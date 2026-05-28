package com.example.goaltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.goaltracker.ui.theme.GoalTrackerTheme
import com.example.goaltracker.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GoalTrackerTheme {
                MainScreen()
            }
        }
    }
}
