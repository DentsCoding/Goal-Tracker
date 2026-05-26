package com.example.goaltracker.ui_elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.goaltracker.view_models.GoalViewModel

@Composable
fun GoalScreen(viewModel: GoalViewModel = viewModel()) {

    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val goalState by viewModel.goalState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Goal title") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Goal description") }
        )

        Button(
            onClick = {
                viewModel.addGoal(title, description)
                title = ""
                description = ""
            }
        ) {
            Text("Add Something")
        }

        LazyColumn() {
            items(goalState) { item ->
                Column() {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }
        }
    }

}