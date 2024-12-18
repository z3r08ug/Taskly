package com.z3r08ug.taskly.presentation.util

import androidx.lifecycle.SavedStateHandle
import com.z3r08ug.data.model.TaskDto
import com.z3r08ug.data.model.toDomain
import com.z3r08ug.data.model.toDto
import com.z3r08ug.domain.model.Task

fun SavedStateHandle.putTask(key: String, task: Task?) {
    task?.toDto()?.let { this[key] = it }
}

fun SavedStateHandle.getTask(key: String): Task? {
    return this.get<TaskDto>(key)?.toDomain()
}
