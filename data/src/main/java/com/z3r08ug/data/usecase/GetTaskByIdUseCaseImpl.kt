package com.z3r08ug.data.usecase

import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.repository.TaskRepository
import com.z3r08ug.domain.usecase.GetTaskByIdUseCase
import javax.inject.Inject

class GetTaskByIdUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : GetTaskByIdUseCase {
    override suspend fun invoke(taskId: Int): Task? {
        return taskRepository.getTaskById(taskId)
    }
}