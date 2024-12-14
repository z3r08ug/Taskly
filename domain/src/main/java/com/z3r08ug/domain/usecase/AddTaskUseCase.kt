package com.z3r08ug.domain.usecase

import com.z3r08ug.domain.model.Task

interface AddTaskUseCase {
    suspend operator fun invoke(task: Task)
}