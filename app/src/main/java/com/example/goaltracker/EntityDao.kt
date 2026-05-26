package com.example.goaltracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


//TODO(): Create DAO package and split those into their own DAOs
@Dao
interface GoalDao{
    // goals
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createGoal(goal: GoalEntity)

    @Query("SELECT * FROM goals")
    fun getAllGoals(): Flow<List<GoalEntity>>
}

@Dao
interface MilestoneDao {
    @Insert
    suspend fun createMilestone(milestone: MilestoneEntity)
}

@Dao
interface TaskTemplateDao {
    @Insert
    suspend fun createTaskTemplate(taskTemplate: TaskTemplateEntity)
}

@Dao
interface TaskDao {
    @Insert
    suspend fun createTask(task: TaskEntity)
}