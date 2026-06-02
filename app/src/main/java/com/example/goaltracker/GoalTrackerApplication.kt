package com.example.goaltracker


import android.app.Application
import androidx.room.Room
import com.example.goaltracker.data.local.GoalDatabase

class GoalTrackerApplication : Application() {
    // Lazy initialization ensures the DB is only created when needed
    val database: GoalDatabase by lazy {
        Room.databaseBuilder(
            this,
            GoalDatabase::class.java,
            "goal_tracker_database"
        )
            // Note: For now, if you change your Room entities, this will clear the DB.
            .fallbackToDestructiveMigration()
            .build()
    }
}