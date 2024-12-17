package com.z3r08ug.domain.model

sealed class TaskFilter(val title: String) {
    object All : TaskFilter("All")
    object ToDo : TaskFilter("ToDo")
    object Completed : TaskFilter("Completed")
}