package com.example.goaltracker.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate


data class MilestoneWithTasks(
    @Embedded val milestone: MilestoneEntity,
    @Relation(
        parentColumn = "milestoneId",    // The primary key in MilestoneEntity
        entityColumn = "milestoneId" // The foreign key in TaskEntity
    )
    val tasks: List<TaskEntity>
)
data class GoalWithDetails(
    @Embedded val goal: GoalEntity,

    @Relation(
        parentColumn = "goalId",
        entityColumn = "goalId"
    )
    val timelines: List<TimelineEntity>,

    @Relation(
        entity = MilestoneEntity::class,
        parentColumn = "goalId",
        entityColumn = "goalId"
    )
    val milestones: List<MilestoneWithTasks>
)
@Dao
interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: GoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeline(timeline: TimelineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: MilestoneEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimelineMilestoneCrossRef(crossRef: TimelineMilestoneCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Transaction
    @Query("SELECT * FROM goals")
    fun getAllGoalsWithDetails(): Flow<List<GoalWithDetails>>

    @Query("SELECT * FROM tasks WHERE milestoneId = :milestoneId")
    fun getTasksForMilestone(milestoneId: String): Flow<List<TaskEntity>>

    // Combined transaction to guarantee atomic auto-creation
    @Transaction
    suspend fun createMilestoneWithTimeline(
        milestone: MilestoneEntity,
        startDate: LocalDate,
        endDate: LocalDate
    ) {
        val timeline = TimelineEntity(
            timelineId = java.util.UUID.randomUUID().toString(),
            goalId = milestone.goalId,
            startDate = startDate,
            endDate = endDate
        )
        insertMilestone(milestone)
        insertTimeline(timeline)
        insertTimelineMilestoneCrossRef(TimelineMilestoneCrossRef(timeline.timelineId, milestone.milestoneId))
    }
}
