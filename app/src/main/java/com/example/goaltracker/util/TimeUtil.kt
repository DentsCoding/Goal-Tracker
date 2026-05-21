package com.example.goaltracker.util

import java.util.Calendar

fun getCurrentTimeMin(): Int {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return hour * 60 + minute
}