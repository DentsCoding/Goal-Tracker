package com.example.goaltracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    GoalEntity::class,
    MilestoneEntity::class,
    TaskTemplateEntity::class,
    TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun taskTemplateDao(): TaskTemplateDao
    abstract fun taskDao(): TaskDao

    companion object { // basically "static" keyword
        @Volatile // makes sure any modification to this variable is immediately seen
                  // by all CPU cores to prevent multiple from trying to create the db
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // ?: (elvis operator) means if left hand side is null use right hand side
            // synchronized also deals with race conditions
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "goal_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}