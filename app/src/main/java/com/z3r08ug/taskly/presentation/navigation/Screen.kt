package com.z3r08ug.taskly.presentation.navigation

sealed class Screen(
    val route: String,
    val title: String,
) {
    data object Tasks : Screen(
        route = "tasks",
        title = "Tasks",
    )

    data object AddTask : Screen(
        route = "add_task",
        title = "Add Task",
    )

    data object TaskDetails : Screen(
        route = "task_details/{taskId}",
        title = "Task Details",
    )
}