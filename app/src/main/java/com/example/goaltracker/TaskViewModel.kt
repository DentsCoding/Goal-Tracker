package com.example.goaltracker

import androidx.lifecycle.ViewModel
import com.example.goaltracker.util.getCurrentTimeMin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class TaskUiState(
    val currentTask: Task? = null
)

// the viewmodel takes care of the state, it wraps the data
// in an uiState and presents it to compose

class TaskViewModel : ViewModel() {

    // with view models we generally create a mutable type of the var
    // that can be mutated by the model
    // and an immutable type that can be accessed from outside

    // a state flow is like a live stream of data

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = TaskUiState(
            // mock a single hardcoded task
            currentTask = Task(
                id = UUID.randomUUID().toString(),
                title = "Read Kotlin Documentation",
                description = "Learn about View Models and State Flows",
                startTime = 540, // 9:00 AM (60 minutes * 9)
                allocatedTime = 45 // Minutes
            )
        )
    }

        // User events

    fun startTask() {
        _uiState.update { currentState ->
            currentState.copy(
                currentTask = currentState.currentTask?.copy(
                    status = TaskStatus.InProgress(actualStartTime = getCurrentTimeMin())
                )
            )
        }
    }

    fun completeTask() {
        _uiState.update { currentState ->
            currentState.copy(
                currentTask = currentState.currentTask?.copy(
                    status = TaskStatus.Completed(completedAt = getCurrentTimeMin())
                )
            )
        }
    }
}