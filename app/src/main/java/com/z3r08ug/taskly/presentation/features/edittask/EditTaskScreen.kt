package com.z3r08ug.taskly.presentation.features.edittask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.z3r08ug.domain.model.Task
import com.z3r08ug.taskly.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    viewModel: EditTaskViewModel = hiltViewModel(),
    onNavigateBack: (Task?) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_task)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack(
                            null
//                            Task(
//                                id = taskId,
//                                title = uiState.title,
//                                description = uiState.description,
//                                isCompleted = uiState.isCompleted
//                            )
                        )
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(dimensionResource(R.dimen.padding_m))
        ) {
            TextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text(stringResource(R.string.title)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_s)))
            TextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_s)))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.isCompleted,
                    onCheckedChange = viewModel::onCompletionChange
                )
                Text(stringResource(R.string.completed))
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_m)))
            Button(
                onClick = { viewModel.saveTask(taskId, { onNavigateBack(null) }) },
                enabled = uiState.isSaveEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete_task))
            }
        }
    }
    // Confirmation Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete_task)) },
            text = { Text(stringResource(R.string.delete_task_confirmation)) },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    viewModel.deleteTask { deletedTask ->
                        onNavigateBack(deletedTask) // Pass back to TasksScreen
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
