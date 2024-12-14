package com.z3r08ug.domain.usecase

import com.z3r08ug.domain.model.Task

interface DeleteTaskUseCase {
    suspend operator fun invoke(task: Task)
}