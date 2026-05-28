package com.example.goaltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.goaltracker.TaskStatus

//TODO(): Create entities package and split those into their own files

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val creationDate: Long = System.currentTimeMillis()
)

@Entity(tableName = "milestones")
data class MilestoneEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val parentId: Long,
    val title: String,
    val targetDate: Long,
    val sequenceNumber: Int = 0
)

@Entity(tableName = "task_templates")
data class TaskTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val milestoneId: Long? = null,
    val title: String,
    val cue: String,
    val suggestedTime: String,
    val suggestedDuration: Int,
    val repeatDays: String
)

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val templateId: Long? = null,
    val date: Long,
    val title: String,
    val cue: String,
    val actualTime: String,
    val actualDuration: Int,
    val isDetached: Boolean = false,
    val status: TaskStatus = TaskStatus.PENDING
)