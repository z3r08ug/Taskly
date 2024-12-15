package com.z3r08ug.data.usecase

import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.repository.TaskRepository
import com.z3r08ug.domain.usecase.DeleteTaskUseCase
import javax.inject.Inject

class DeleteTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : DeleteTaskUseCase {
    override suspend fun invoke(task: Task) {
        taskRepository.deleteTask(task)
    }
}