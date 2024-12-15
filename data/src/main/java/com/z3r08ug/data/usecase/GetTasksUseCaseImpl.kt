package com.z3r08ug.data.usecase

import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.repository.TaskRepository
import com.z3r08ug.domain.usecase.GetTasksUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : GetTasksUseCase {
    override fun invoke(): Flow<List<Task>> {
        return taskRepository.getTasks()
    }
}