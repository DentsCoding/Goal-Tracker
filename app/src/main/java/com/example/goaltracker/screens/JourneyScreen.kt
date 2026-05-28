package com.example.goaltracker.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goaltracker.view_models.JourneyViewModel

@Composable
fun JourneyScreen(viewModel: JourneyViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "The Journey Creator",
            style = MaterialTheme.typography.headlineMedium
        )

        // SECTION 1: CREATE CAMPAIGN
        CampaignCreatorCard(onCreate = { title, desc ->
            viewModel.createCampaign(title, desc)
        })

        // SECTION 2: TASK TEMPLATE GENERATOR
        TemplateCreatorCard(onCreate = { title, cue, time, duration ->
            // For prototyping simplicity, we pass null milestone & set it to repeat Mon/Wed/Fri ("2,4,6")
            viewModel.createTaskTemplate(
                milestoneId = null,
                title = title,
                cue = cue,
                time = time,
                duration = duration,
                daysOfWeek = listOf(2, 4, 6)
            )
        })
    }
}

@Composable
fun CampaignCreatorCard(onCreate: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Step 1: Start a New Campaign", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Campaign Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, desc)
                        title = ""
                        desc = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lock in Campaign")
            }
        }
    }
}

@Composable
fun TemplateCreatorCard(onCreate: (String, String, String, Int) -> Unit) {
    var title by remember { mutableStateOf("") }
    var cue by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("09:00") }
    var duration by remember { mutableStateOf("30") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Step 2: Add Blueprint Task (Mon/Wed/Fri)", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Task Title") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = cue, onValueChange = { cue = it }, label = { Text("Environment Cue (e.g. 'When I sit down')") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Start Time (HH:MM)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = duration, onValueChange = { duration = it }, label = { Text("Duration (mins)") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onCreate(title, cue, time, duration.toIntOrNull() ?: 30)
                        title = ""
                        cue = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Deploy Template to Schedule")
            }
        }
    }
}