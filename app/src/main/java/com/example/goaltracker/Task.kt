package com.example.goaltracker

import java.time.LocalTime
import kotlin.time.Duration

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val startTime: LocalTime,
    val allocatedTime: Duration,
    val status: TaskStatus = TaskStatus.NotStarted
)


sealed class TaskStatus {
    object NotStarted : TaskStatus()
    data class InProgress(val actualStartTime: LocalTime) : TaskStatus()
    data class Completed(val completedAt: LocalTime) : TaskStatus()
}