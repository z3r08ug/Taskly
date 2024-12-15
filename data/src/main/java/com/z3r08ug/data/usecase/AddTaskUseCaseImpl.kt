package com.z3r08ug.data.usecase

import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.repository.TaskRepository
import com.z3r08ug.domain.usecase.AddTaskUseCase
import javax.inject.Inject

class AddTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository
) : AddTaskUseCase {
    override suspend fun invoke(task: Task) {
        taskRepository.insertTask(task)
    }
}