package com.z3r08ug.data.model

import android.os.Parcelable
import com.z3r08ug.domain.model.Task
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskDto(
    val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean
) : Parcelable

// Convert TaskDto -> Task (Domain Model)
fun TaskDto.toDomain(): Task = Task(
    id = this.id,
    title = this.title,
    description = this.description,
    isCompleted = this.isCompleted
)

// Convert Task -> TaskDto (DTO)
fun Task.toDto(): TaskDto = TaskDto(
    id = this.id,
    title = this.title,
    description = this.description,
    isCompleted = this.isCompleted
)