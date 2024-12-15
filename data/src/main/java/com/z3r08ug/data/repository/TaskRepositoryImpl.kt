package com.z3r08ug.data.repository

import com.z3r08ug.data.dao.TaskDao
import com.z3r08ug.data.entity.TaskEntity
import com.z3r08ug.domain.model.Task
import com.z3r08ug.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override fun getTasks(): Flow<List<Task>> =
        taskDao.getTasks().map { entities ->
            entities.map { entity ->
                Task(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    isCompleted = entity.isCompleted,
                )
            }
        }

    override suspend fun insertTask(task: Task) {
        taskDao.insertTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = task.isCompleted
            )
        )
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = task.isCompleted
            )
        )
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(
            TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                isCompleted = task.isCompleted
            )
        )
    }
}