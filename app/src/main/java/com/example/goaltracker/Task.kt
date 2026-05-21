package com.example.goaltracker

import java.time.LocalTime
import kotlin.time.Duration

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val startTime: Int,
    val allocatedTime: Int,
    val status: TaskStatus = TaskStatus.NotStarted
)


sealed class TaskStatus {
    object NotStarted : TaskStatus()
    data class InProgress(val actualStartTime: Int) : TaskStatus()
    data class Completed(val completedAt: Int) : TaskStatus()
}