package com.z3r08ug.taskly.presentation.features.tasks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import com.z3r08ug.domain.model.Task
import com.z3r08ug.taskly.R

@Composable
fun TaskItem(
    task: Task,
    isSelected: Boolean,
    onSelectionChanged: (Int) -> Unit,
    onTaskClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Animate opacity based on task completion state
    val alpha by animateFloatAsState(
        targetValue = if (task.isCompleted) 0.5f else 1f
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClicked(task.id) }
            .padding(dimensionResource(R.dimen.padding_m))
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onSelectionChanged(task.id) }
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_s)))
        Column {
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                modifier = Modifier.alpha(alpha) // Apply animated opacity
            )
            if (task.description.isNotEmpty()) {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.alpha(alpha) // Apply animated opacity
                )
            }
        }
    }
}