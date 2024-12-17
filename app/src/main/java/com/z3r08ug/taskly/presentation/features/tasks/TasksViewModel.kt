package com.z3r08ug.taskly.presentation.features.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.model.TaskFilter
import com.z3r08ug.domain.usecase.GetTasksUseCase
import com.z3r08ug.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _selectedTaskIds = MutableStateFlow<Set<Int>>(emptySet())
    private val _filter = MutableStateFlow<TaskFilter>(TaskFilter.All)

    val uiState: StateFlow<TasksUiState> = combine(
        _tasks, _selectedTaskIds, _filter
    ) { tasks, selectedIds, filter ->
        val filteredTasks = when (filter) {
            TaskFilter.All -> tasks
            TaskFilter.ToDo -> tasks.filter { !it.isCompleted }
            TaskFilter.Completed -> tasks.filter { it.isCompleted }
        }
        TasksUiState(filteredTasks, selectedIds, filter)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, TasksUiState())

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            getTasksUseCase.invoke()
                .collect { tasks ->
                    _tasks.value = tasks
                }
        }
    }

    fun toggleTaskSelection(taskId: Int) {
        _selectedTaskIds.update { ids ->
            if (taskId in ids) ids - taskId else ids + taskId
        }
    }

    fun markSelectedTasksAsCompleted(completed: Boolean) {
        viewModelScope.launch {
            _tasks.value.filter { it.id in _selectedTaskIds.value }
                .map { it.copy(isCompleted = completed) }
                .forEach { updateTaskUseCase(it) }
            _selectedTaskIds.value = emptySet()
        }
    }

    fun setFilter(filter: TaskFilter) {
        _filter.value = filter
    }
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val selectedTaskIds: Set<Int> = emptySet(),
    val filter: TaskFilter = TaskFilter.All
)