package com.z3r08ug.domain.usecase

import com.z3r08ug.domain.model.Task

interface GetTaskByIdUseCase {
    suspend operator fun invoke(taskId: Int): Task?
}