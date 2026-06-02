package com.example.goaltracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goaltracker.data.MilestoneEntity
import com.example.goaltracker.view_models.ManagementViewModel

import androidx.room.Embedded
import androidx.room.Relation
import com.example.goaltracker.data.GoalEntity
import com.example.goaltracker.data.TaskEntity

data class MilestoneWithTasks(
    @Embedded val milestone: MilestoneEntity,
    @Relation(
        parentColumn = "milestoneId",
        entityColumn = "milestoneOwnerId"
    )
    val tasks: List<TaskEntity>
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    viewModel: ManagementViewModel = viewModel()
) {
    val goalsList by viewModel.goalsState.collectAsState(initial = emptyList())

    var showGoalDialog by remember { mutableStateOf(false) }

    // Tracking active selections for creation paths
    var selectedGoalIdForMilestone by remember { mutableStateOf<String?>(null) }
    var selectedMilestoneForTask by remember { mutableStateOf<MilestoneEntity?>(null) }
    var selectedGoalForMilestone by remember { mutableStateOf<GoalEntity?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Goal Architecture") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showGoalDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(goalsList) { goalWithDetails -> // Fixed to your goalList name
                GoalExpandableCard(
                    goalWithDetails = goalWithDetails,
                    onAddMilestoneClick = {
                        // Now this will work perfectly!
                        selectedGoalForMilestone = goalWithDetails.goal
                    },
                    onMilestoneClick = { },
                    onAddTaskClick = { milestone ->
                        selectedMilestoneForTask = milestone
                    }
                )
            }
        }

        // Dialog 1: Create Goal
        if (showGoalDialog) {
            CreateGoalDialog(
                onDismiss = { showGoalDialog = false },
                onConfirm = { title, start, end ->
                    viewModel.createGoal(title, start, end)
                    showGoalDialog = false
                }
            )
        }

        // Dialog 2: Create Milestone (+ Auto Timeline)
        selectedGoalIdForMilestone?.let { goalId ->
            CreateMilestoneDialog(
                onDismiss = { selectedGoalIdForMilestone = null },
                onConfirm = { title, start, end ->
                    viewModel.createMilestone(goalId, title, start, end)
                    selectedGoalIdForMilestone = null
                }
            )
        }

        // Dialog 3: Create Task within Milestone
        selectedMilestoneForTask?.let { milestone ->
            CreateTaskDialog(
                milestone = milestone,
                onDismiss = { selectedMilestoneForTask = null }, // Clears state to hide dialog
                onConfirm = { title, date ->
                    viewModel.createTask(milestone.milestoneId, title, date)
                    selectedMilestoneForTask = null // Closes dialog after saving
                }
            )
        }
    }
}

