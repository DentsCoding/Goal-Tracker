package com.example.goaltracker.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.goaltracker.AppDatabase
import com.example.goaltracker.GoalEntity
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

    fun addGoal(title: String, description: String) {
        viewModelScope.launch {
            val newGoal = GoalEntity(title = title, description = description)
            goalDao.createGoal(newGoal)
        }
    }
}