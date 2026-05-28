package com.example.goaltracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goaltracker.view_models.GoalViewModel

@Composable
fun GoalScreen(viewModel: GoalViewModel = viewModel()) {
    // Collect both the campaigns and the daily schedule items
    val campaigns by viewModel.campaignsState.collectAsState()
    val calendarItems by viewModel.calendarItemsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- SECTION 1: THE ACTIVE CAMPAIGN BANNER ---
        val activeCampaign = campaigns.lastOrNull() // Grabs the latest campaign created

        if (activeCampaign != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Campaign:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = activeCampaign.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    if (activeCampaign.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = activeCampaign.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        } else {
            // Placeholder warning if no campaign has been made yet
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "⚠️ No active campaign found. Go to the Journey tab to lock one in!",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        Divider()

        // --- SECTION 2: TODAY'S SCHEDULE ---
        Text(
            text = "Today's Focus Tasks",
            style = MaterialTheme.typography.titleLarge
        )

        if (calendarItems.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No tasks scheduled for today. Take a breather!")
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calendarItems) { item ->
                    val cardAlpha = if (item.isGhost) 0.6f else 1.0f

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(cardAlpha),
                        colors = CardDefaults.cardColors(
                            containerColor = if (item.isCompleted) MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${item.displayTime} - ${item.title}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                if (item.cue.isNotEmpty()) {
                                    Text(
                                        text = "Cue: ${item.cue}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { viewModel.toggleTaskCompletion(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}