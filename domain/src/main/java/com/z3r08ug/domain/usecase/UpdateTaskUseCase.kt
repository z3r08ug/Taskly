package com.z3r08ug.domain.usecase

import com.z3r08ug.domain.model.Task

interface UpdateTaskUseCase {
    suspend operator fun invoke(task: Task)
}