package com.z3r08ug.taskly.presentation.features.tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.z3r08ug.domain.model.TaskFilter
import com.z3r08ug.taskly.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    onAddTaskClicked: () -> Unit,
    onTaskClicked: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val tabs = listOf(TaskFilter.All, TaskFilter.ToDo, TaskFilter.Completed)
    val selectedTabIndex = tabs.indexOf(uiState.filter)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddTaskClicked() }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_task))
            }
        },
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            // Tab View for Filtering
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, filter ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.setFilter(filter) },
                        text = { Text(filter.title) }
                    )
                }
            }

            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.tasks) { task ->
                    TaskItem(
                        task = task,
                        isSelected = task.id in uiState.selectedTaskIds,
                        onSelectionChanged = viewModel::toggleTaskSelection,
                        onTaskClicked = onTaskClicked
                    )
                }
            }

            // Action Buttons
            if (uiState.selectedTaskIds.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { viewModel.markSelectedTasksAsCompleted(true) }) {
                        Text(stringResource(R.string.mark_completed))
                    }
                    Button(onClick = { viewModel.markSelectedTasksAsCompleted(false) }) {
                        Text(stringResource(R.string.mark_todo))
                    }
                }
            }
        }
    }
}