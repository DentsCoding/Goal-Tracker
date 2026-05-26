package com.example.goaltracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDao{
    // goals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createGoal(goal: GoalEntity)

    @Query("SELECT * from goals")
    fun getAllGoals(): Flow<List<GoalEntity>>
}

interface MilestoneDao {
    @Insert
    suspend fun createMilestone(milestone: MilestoneEntity)
}

interface TaskTemplateDao {
    @Insert
    suspend fun createTaskTemplate(taskTemplate: TaskTemplateEntity)
}

interface TaskDao {
    @Insert
    suspend fun createTask(task: TaskEntity)
}