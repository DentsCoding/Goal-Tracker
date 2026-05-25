package com.example.goaltracker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val hexColor: String
)


@Entity(tableName = "routine_templates")
data class RoutineTemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val campaignId: Int, // links back to the campaign
    val name: String,
    val defaultDurationMinutes: Int,
    val scheduledTime: String?,
    val repeatDays: String
)

@Entity(tableName = "milestone_task")
data class MilestoneTaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val campaignId: Int,
    val milestoneTitle: String,
    val taskName: String,
    val dueDateEpochDays: Long?
)

@Entity(tableName = "milestone_task")
data class DailyInstanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val campaignId: Int,
    val title: String,
    val isRoutine: Boolean,
    val durationMinutes: Int,
    val startTime: String?,
    val dateEpochDays: Long,
    val timeTrackedSeconds: Long = 0L,
    val isCompleted: Boolean = false
)

