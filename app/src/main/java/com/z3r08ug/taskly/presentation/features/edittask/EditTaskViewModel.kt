package com.z3r08ug.taskly.presentation.features.edittask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z3r08ug.domain.usecase.GetTaskByIdUseCase
import com.z3r08ug.domain.usecase.UpdateTaskUseCase
import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.usecase.DeleteTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUiState())
    val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()
    private var currentTask: Task? = null // Hold the loaded task for reference

    fun loadTask(taskId: Int) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase(taskId)
            if (task != null) {
                currentTask = task // Update the reference
                _uiState.value = _uiState.value.copy(
                    title = task.title,
                    description = task.description,
                    isCompleted = task.isCompleted,
                    isSaveEnabled = task.title.isNotBlank()
                )
            }
        }
    }

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(
            title = newTitle,
            isSaveEnabled = newTitle.isNotBlank()
        )
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.value = _uiState.value.copy(description = newDescription)
    }

    fun onCompletionChange(isCompleted: Boolean) {
        _uiState.value = _uiState.value.copy(isCompleted = isCompleted)
    }

    fun saveTask(taskId: Int, onTaskSaved: () -> Unit) {
        viewModelScope.launch {
            val updatedTask = Task(
                id = taskId,
                title = _uiState.value.title,
                description = _uiState.value.description,
                isCompleted = _uiState.value.isCompleted
            )
            updateTaskUseCase(updatedTask)
            onTaskSaved()
        }
    }

    fun deleteTask(onTaskDeleted: (Task) -> Unit) {
        viewModelScope.launch {
            currentTask?.let { task ->
                deleteTaskUseCase.invoke(task) // Remove from database
                onTaskDeleted(task) // Pass deleted task back
            }
        }
    }
}

data class EditTaskUiState(
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val isSaveEnabled: Boolean = false
)