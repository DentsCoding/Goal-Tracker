package com.example.goaltracker.view_models


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.goaltracker.data.AppDatabase
import com.example.goaltracker.data.GoalEntity
import com.example.goaltracker.data.MilestoneEntity
import com.example.goaltracker.data.TaskTemplateEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JourneyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val goalDao = database.goalDao()
    private val milestoneDao = database.milestoneDao()
    private val templateDao = database.taskTemplateDao()

    // Expose all goals so the user can select which goal/campaign they are adding milestones to
    val allGoals: StateFlow<List<GoalEntity>> = goalDao.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createCampaign(title: String, description: String) {
        viewModelScope.launch {
            goalDao.createGoal(GoalEntity(title = title, description = description))
        }
    }

    fun createMilestone(goalId: Long, title: String, targetDate: Long) {
        viewModelScope.launch {
            milestoneDao.createMilestone(
                MilestoneEntity(
                    parentId = goalId,
                    title = title,
                    targetDate = targetDate
                )
            )
        }
    }

    fun createTaskTemplate(
        milestoneId: Long?,
        title: String,
        cue: String,
        time: String,
        duration: Int,
        daysOfWeek: List<Int>
    ) {
        viewModelScope.launch {
            // Convert List<Int> like [2, 4] to a clean string "2,4" for Room compatibility
            val daysString = daysOfWeek.joinToString(",")

            templateDao.createTaskTemplate(
                TaskTemplateEntity(
                    milestoneId = milestoneId,
                    title = title,
                    cue = cue,
                    suggestedTime = time,
                    suggestedDuration = duration,
                    repeatDays = daysString
                )
            )
        }
    }
}