package com.z3r08ug.domain.usecase

import com.z3r08ug.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface GetTasksUseCase {
    operator fun invoke(): Flow<List<Task>>
}