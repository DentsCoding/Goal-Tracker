// File: data/local/GoalDatabase.kt
package com.example.goaltracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.goaltracker.data.GoalDao
import com.example.goaltracker.data.GoalEntity
import com.example.goaltracker.data.MilestoneEntity
import com.example.goaltracker.data.TaskEntity
import com.example.goaltracker.data.TimelineEntity
import com.example.goaltracker.data.TimelineMilestoneCrossRef

@Database(
    entities = [
        GoalEntity::class,
        TimelineEntity::class,
        MilestoneEntity::class,
        TimelineMilestoneCrossRef::class,
        TaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class) // Essential for handling your dates!
abstract class GoalDatabase : RoomDatabase() {
    abstract fun goalDao(): GoalDao
}