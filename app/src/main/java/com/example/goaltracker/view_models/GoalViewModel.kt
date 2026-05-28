package com.example.goaltracker.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.goaltracker.data.AppDatabase
import com.example.goaltracker.data.TaskEntity
import com.example.goaltracker.TaskStatus
import com.example.goaltracker.data.GoalEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import java.util.Calendar

// The unified model your Compose UI will actually read
data class CalendarTaskItem(
    val id: Long,               // Template ID if ghost, Task ID if actual saved task
    val templateId: Long?,
    val title: String,
    val cue: String,
    val displayTime: String,
    val duration: Int,
    val isCompleted: Boolean,
    val isGhost: Boolean        // True if it's a template placeholder, False if saved in DB
)

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val taskDao = database.taskDao()
    private val templateDao = database.taskTemplateDao()

    // Keeps track of what day the user has selected (default to today's midnight)
    private val _selectedDate = MutableStateFlow(getMidnightTimestamp(System.currentTimeMillis()))
    val selectedDate: StateFlow<Long> = _selectedDate

    // The core pipeline: Reacts whenever the database OR the selected date changes!
    val calendarItemsState: StateFlow<List<CalendarTaskItem>> = combine(
        templateDao.getAllTemplates(),
        _selectedDate
    ) { templates, date ->
        // 1. Find the day of the week for our database check (1 = Sun, 2 = Mon... 7 = Sat)
        val cal = Calendar.getInstance().apply { timeInMillis = date }
        val dayOfWeekStr = cal.get(Calendar.DAY_OF_WEEK).toString()

        // 2. Get the real tasks for this day (Room allows synchronous reading inside Flow operators)
        // Note: For a fast prototype, this is perfectly fine!
        val actualTasks = database.taskDao().getTasksForDate(date).stateIn(viewModelScope).value

        val mergedList = mutableListOf<CalendarTaskItem>()
        val actualTemplateIds = actualTasks.mapNotNull { it.templateId }

        // Add actual saved tasks
        actualTasks.forEach { task ->
            mergedList.add(
                CalendarTaskItem(
                    id = task.id,
                    templateId = task.templateId,
                    title = task.title,
                    cue = task.cue,
                    displayTime = task.actualTime,
                    duration = task.actualDuration,
                    isCompleted = task.status == TaskStatus.COMPLETED,
                    isGhost = false
                )
            )
        }

        // Add templates as "ghosts" if they match today's day-of-week and haven't been completed yet
        templates.forEach { template ->
            val runsToday = template.repeatDays.split(",").contains(dayOfWeekStr)
            if (runsToday && !actualTemplateIds.contains(template.id)) {
                mergedList.add(
                    CalendarTaskItem(
                        id = template.id,
                        templateId = template.id,
                        title = template.title,
                        cue = template.cue,
                        displayTime = template.suggestedTime,
                        duration = template.suggestedDuration,
                        isCompleted = false,
                        isGhost = true
                    )
                )
            }
        }

        mergedList.sortedBy { it.displayTime }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // When user checks a box or edits
    fun toggleTaskCompletion(item: CalendarTaskItem) {
        viewModelScope.launch {
            if (item.isGhost) {
                // Instantiating the ghost into reality!
                val newTask = TaskEntity(
                    templateId = item.templateId,
                    date = _selectedDate.value,
                    title = item.title,
                    cue = item.cue,
                    actualTime = item.displayTime,
                    actualDuration = item.duration,
                    status = TaskStatus.COMPLETED
                )
                taskDao.createTask(newTask)
            } else {
                // Already exists, just flip it
                val nextStatus = if (item.isCompleted) "PENDING" else "COMPLETED"
                taskDao.updateTaskStatus(item.id, nextStatus)
            }
        }
    }

    // Helper to strip hours/minutes out of timestamps so we can group by clean calendar days
    private fun getMidnightTimestamp(millis: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = millis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val campaignsState: StateFlow<List<GoalEntity>> = database.goalDao().getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}