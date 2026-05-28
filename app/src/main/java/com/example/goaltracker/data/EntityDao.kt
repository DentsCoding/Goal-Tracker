package com.example.goaltracker.data

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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTaskTemplate(taskTemplate: TaskTemplateEntity)

    @Query("SELECT * FROM task_templates")
    fun getAllTemplates(): Flow<List<TaskTemplateEntity>>
}

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTask(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE date = :date")
    fun getTasksForDate(date: Long): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET status = :newStatus WHERE id = :taskId")
    fun updateTaskStatus(taskId: Long, newStatus: String)
}