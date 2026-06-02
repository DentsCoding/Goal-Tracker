package com.example.goaltracker.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goaltracker.data.GoalDao
import com.example.goaltracker.data.GoalEntity
import com.example.goaltracker.data.GoalWithDetails
import com.example.goaltracker.data.MilestoneEntity
import com.example.goaltracker.data.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class ManagementViewModel(private val goalDao: GoalDao) : ViewModel() {

    val goalsState: Flow<List<GoalWithDetails>> = goalDao.getAllGoalsWithDetails()

    fun createGoal(title: String, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            val goal = GoalEntity(UUID.randomUUID().toString(), title, startDate, endDate)
            goalDao.insertGoal(goal)
        }
    }

    fun createMilestone(goalId: String, title: String, startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            val milestone = MilestoneEntity(UUID.randomUUID().toString(), goalId, title)
            goalDao.createMilestoneWithTimeline(milestone, startDate, endDate)
        }
    }

    fun createTask(milestoneId: String, title: String, date: LocalDate) {
        viewModelScope.launch {
            val task = TaskEntity(UUID.randomUUID().toString(), milestoneId, title, date)
            goalDao.insertTask(task)
        }
    }
}