package com.z3r08ug.taskly.presentation.features.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.model.TaskFilter
import com.z3r08ug.taskly.R
import com.z3r08ug.taskly.presentation.util.getTask
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavHostController,
    viewModel: TasksViewModel = hiltViewModel(),
    onAddTaskClicked: () -> Unit,
    onTaskClicked: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) } // Show confirmation dialog
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    val deletedTask = savedStateHandle?.getTask("deleted_task")

    val tabs = listOf(TaskFilter.All, TaskFilter.ToDo, TaskFilter.Completed)
    val selectedTabIndex = tabs.indexOf(uiState.filter)

    val taskDeleted = stringResource(R.string.task_deleted)
    val undo = stringResource(R.string.undo)
    LaunchedEffect(deletedTask) {
        if (deletedTask != null) {
            val result = snackbarHostState.showSnackbar(
                message = taskDeleted,
                actionLabel = undo,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoSingleDelete(deletedTask)
            }
            savedStateHandle.remove<Task>("deleted_task") // Clear the value
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddTaskClicked() }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_task))
            }
        },
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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

            //Task List
            LazyColumn(
                contentPadding = padding,
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.tasks, key = { it.id }) { task ->
                    AnimatedVisibility(
                        visible = true,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                    TaskItem(
                        task = task,
                        isSelected = task.id in uiState.selectedTaskIds,
                        onSelectionChanged = viewModel::toggleTaskSelection,
                        onTaskClicked = onTaskClicked,
                    )
                        }
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
                    Button(onClick = { showDialog = true }) { // Show confirmation dialog
                        Text("Delete")
                    }
                }
            }
        }
    }
    if (showDialog) {
        val tasksDeleted = stringResource(R.string.tasks_deleted)
        val undo = stringResource(R.string.undo)
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_tasks)) },
            text = { Text(stringResource(R.string.delete_tasks_confirmation)) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    scope.launch {
                        viewModel.deleteSelectedTasks()
                        val result = snackbarHostState.showSnackbar(
                            message = tasksDeleted,
                            actionLabel = undo,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.undoDelete()
                        }
                    }
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}