package com.z3r08ug.taskly.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.z3r08ug.taskly.presentation.features.addtask.AddTaskScreen
import com.z3r08ug.taskly.presentation.features.edittask.EditTaskScreen
import com.z3r08ug.taskly.presentation.features.tasks.TasksScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        navController = navController,
        startDestination = Screen.Tasks.route
    ) {
        composable(Screen.Tasks.route) {
            TasksScreen(
                onTaskClicked = { taskId ->
                    navController.navigate(Screen.EditTask.route.replace("{taskId}", taskId.toString()))
                },
                onAddTaskClicked = {
                    navController.navigate(Screen.AddTask.route)
                }
            )
        }
        composable(Screen.AddTask.route) {
            AddTaskScreen(
                onReturnToTasks = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.EditTask.route) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            EditTaskScreen(
                taskId = taskId?.toInt() ?: -1,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}