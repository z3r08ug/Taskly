package com.z3r08ug.taskly.presentation.features.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.model.TaskFilter
import com.z3r08ug.domain.usecase.AddTaskUseCase
import com.z3r08ug.domain.usecase.DeleteTaskUseCase
import com.z3r08ug.domain.usecase.GetTasksUseCase
import com.z3r08ug.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _selectedTaskIds = MutableStateFlow<Set<Int>>(emptySet())
    private val _filter = MutableStateFlow<TaskFilter>(TaskFilter.All)
    private var lastDeletedTasks: List<Task> = emptyList() // Temporarily store deleted tasks

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

    fun deleteSelectedTasks() {
        viewModelScope.launch {
            val tasksToDelete = _tasks.value.filter { it.id in _selectedTaskIds.value }
            lastDeletedTasks = tasksToDelete // Store for undo

            tasksToDelete.forEach { task ->
                deleteTaskUseCase(task) // Delete from database
            }

            _tasks.value = _tasks.value.filterNot { it.id in _selectedTaskIds.value }
            _selectedTaskIds.value = emptySet()
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            lastDeletedTasks.forEach { task ->
                addTaskUseCase(task) // Restore task to the database
            }
            _tasks.update { it + lastDeletedTasks } // Add back to in-memory list
            lastDeletedTasks = emptyList() // Clear the deleted tasks list
        }
    }

    fun undoSingleDelete(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task) // Restore task to the database
            _tasks.update { it + task } // Add back to in-memory list
        }
    }
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val selectedTaskIds: Set<Int> = emptySet(),
    val filter: TaskFilter = TaskFilter.All
)