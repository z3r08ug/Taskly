package com.z3r08ug.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.z3r08ug.data.model.TaskDto

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

// Convert TaskEntity -> TaskDto
fun TaskEntity.toDto(): TaskDto = TaskDto(
    id = this.id,
    title = this.title,
    description = this.description,
    isCompleted = this.isCompleted
)

// Convert TaskDto -> TaskEntity
fun TaskDto.toEntity(): TaskEntity = TaskEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    isCompleted = this.isCompleted
)

