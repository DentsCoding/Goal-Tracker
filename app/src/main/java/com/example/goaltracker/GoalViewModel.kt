package com.example.goaltracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    private val goalDao = AppDatabase.getDatabase(application).goalDao()

    val goalState: StateFlow<List<GoalEntity>> = goalDao.getAllGoals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val title = ""
    private val description = ""

    fun addGoal(title: String, description: String) {
        viewModelScope.launch {
            val newGoal = GoalEntity(title = title, description = description)
            goalDao.createGoal(newGoal)
        }
    }
}