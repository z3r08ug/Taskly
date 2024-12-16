package com.z3r08ug.taskly.presentation.features.tasks

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    onAddTaskClicked: () -> Unit,
    onTaskClicked: (Int) -> Unit,
) {
    val tasks by viewModel.tasks.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddTaskClicked() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        topBar = {
            TopAppBar(title = { Text("Taskly") })
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onTaskClicked = { onTaskClicked(task.id) },
                    onToggleCompletion = { viewModel.toggleTaskCompletion(task) }
                )
            }
        }
    }
}