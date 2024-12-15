package com.z3r08ug.data.usecase

import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.repository.TaskRepository
import com.z3r08ug.domain.usecase.UpdateTaskUseCase
import javax.inject.Inject

class UpdateTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : UpdateTaskUseCase {
    override suspend fun invoke(task: Task) {
        taskRepository.updateTask(task)
    }
}