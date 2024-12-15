package com.z3r08ug.data.di

import com.z3r08ug.data.usecase.AddTaskUseCaseImpl
import com.z3r08ug.data.usecase.DeleteTaskUseCaseImpl
import com.z3r08ug.data.usecase.GetTasksUseCaseImpl
import com.z3r08ug.data.usecase.UpdateTaskUseCaseImpl
import com.z3r08ug.domain.usecase.AddTaskUseCase
import com.z3r08ug.domain.usecase.DeleteTaskUseCase
import com.z3r08ug.domain.usecase.GetTasksUseCase
import com.z3r08ug.domain.usecase.UpdateTaskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindAddTaskUseCase(addTaskUseCaseImpl: AddTaskUseCaseImpl): AddTaskUseCase

    @Binds
    abstract fun bindDeleteTaskUseCase(deleteTaskUseCaseImpl: DeleteTaskUseCaseImpl): DeleteTaskUseCase

    @Binds
    abstract fun bindGetTasksUseCase(getTasksUseCaseImpl: GetTasksUseCaseImpl): GetTasksUseCase

    @Binds
    abstract fun bindUpdateTaskUseCase(updateTaskUseCaseImpl: UpdateTaskUseCaseImpl): UpdateTaskUseCase
}
