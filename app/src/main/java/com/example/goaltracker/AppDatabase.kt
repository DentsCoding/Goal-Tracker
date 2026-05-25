package com.example.goaltracker

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CampaignEntity::class, RoutineTemplateEntity::class, MilestoneTaskEntity::class, DailyInstanceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun campaignEntityDao(): CampaignEntity
    abstract fun routineTemplateEntityDao(): RoutineTemplateEntity
    abstract fun milestoneTaskEntityDao(): MilestoneTaskEntity
    abstract fun dailyInstanceEntity(): DailyInstanceEntity

    companion object { // basically "static" keyword
        @Volatile // makes sure any modification to this variable is immediately seen
                  // by all CPU cores to prevent multiple from trying to create the db
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // ?: (elivs operator) means if left hand side is null use right hand side
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