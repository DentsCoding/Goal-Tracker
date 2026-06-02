// File: MainActivity.kt
package com.example.goaltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.goaltracker.screens.ManagementScreen
import com.example.goaltracker.ui.theme.GoalTrackerTheme // Your app's generated theme name
import com.example.goaltracker.view_models.ManagementViewModel

class MainActivity : ComponentActivity() {

    // Inject the database DAO using a custom Factory
    private val managementViewModel: ManagementViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = (application as GoalTrackerApplication).database
                @Suppress("UNCHECKED_CAST")
                return ManagementViewModel(database.goalDao()) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoalTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Right now, loading Screen 2 directly to test creation and browsing!
                    ManagementScreen(viewModel = managementViewModel)
                }
            }
        }
    }
}