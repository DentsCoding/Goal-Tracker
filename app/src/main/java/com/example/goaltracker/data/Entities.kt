package com.example.goaltracker.data

import androidx.room.*
import java.time.LocalDate

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey val goalId: String,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate
)

@Entity(
    tableName = "timelines",
    foreignKeys = [ForeignKey(
        entity = GoalEntity::class,
        parentColumns = ["goalId"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TimelineEntity(
    @PrimaryKey val timelineId: String,
    val goalId: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isOverflowResolved: Boolean = false
)

@Entity(
    tableName = "milestones",
    foreignKeys = [ForeignKey(
        entity = GoalEntity::class,
        parentColumns = ["goalId"],
        childColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MilestoneEntity(
    @PrimaryKey val milestoneId: String,
    val goalId: String,
    val title: String
)

// Junction table because a Timeline can have multiple Milestones (due to moving tasks),
// and a Milestone's tasks could theoretically span or be pushed into later Timelines.
@Entity(
    tableName = "timeline_milestone_cross_ref",
    primaryKeys = ["timelineId", "milestoneId"]
)
data class TimelineMilestoneCrossRef(
    val timelineId: String,
    val milestoneId: String
)

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = MilestoneEntity::class,
        parentColumns = ["milestoneId"],
        childColumns = ["milestoneId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TaskEntity(
    @PrimaryKey val taskId: String,
    val milestoneId: String,
    val title: String,
    val assignedDate: LocalDate,
    val isCompleted: Boolean = false,
    val isArchived: Boolean = false
)