package com.z3r08ug.taskly.presentation.features.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.usecase.AddTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddTaskUiState())
    val uiState: StateFlow<AddTaskUiState> = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(
            title = newTitle,
            isSaveEnabled = newTitle.isNotBlank()
        )
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.value = _uiState.value.copy(description = newDescription)
    }

    fun saveTask(onTaskSaved: () -> Unit) {
        viewModelScope.launch {
            val task = Task(
                id = 0, // Room will auto-generate the ID.
                title = _uiState.value.title,
                description = _uiState.value.description,
                isCompleted = false
            )
            addTaskUseCase(task)
            onTaskSaved() // Trigger navigation back.
        }
    }
}

data class AddTaskUiState(
    val title: String = "",
    val description: String = "",
    val isSaveEnabled: Boolean = false
)