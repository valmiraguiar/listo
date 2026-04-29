package com.valmiraguiar.listo.feature.tasks.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.core.navigation.ListoDestination
import com.valmiraguiar.listo.feature.tasks.domain.model.TaskStatus
import com.valmiraguiar.listo.ui.theme.ListoTheme

@Composable
fun TaskDetailsRoute(
    destination: ListoDestination.TaskDetails,
    onBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<TaskDetailsViewModel, TaskDetailsViewModel.Factory>(
        creationCallback = { factory ->
            factory.create(destination)
        },
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TaskDetailsScreen(
        uiState = uiState,
        onBack = onBack,
        modifier = modifier,
    )
}

@Composable
fun TaskDetailsScreen(
    uiState: TaskDetailsUiState,
    onBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    val title = if (uiState.isNotFound) {
        stringResource(id = R.string.task_details_not_found_title)
    } else {
        uiState.title
    }
    val description = if (uiState.isNotFound) {
        stringResource(
            id = R.string.task_details_not_found_description,
            uiState.missingTaskId ?: 0L,
        )
    } else {
        uiState.description
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(id = R.string.task_details_title),
                style = MaterialTheme.typography.headlineMedium,
            )
            OutlinedButton(
                onClick = dropUnlessResumed {
                    onBack()
                },
            ) {
                Text(text = stringResource(id = R.string.action_back))
            }
        }

        uiState.status?.let { status ->
            AssistChip(
                onClick = {},
                label = {
                    Text(text = stringResource(id = status.labelRes()))
                },
            )
        }

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            if (uiState.summary.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskDetailsScreenPreview() {
    ListoTheme {
        TaskDetailsScreen(
            uiState = TaskDetailsUiState(
                title = "Definir stack base",
                summary = "Compose + Navigation 3 + Hilt integrados.",
                description = "Cada tela usa um contrato proprio e pode evoluir sem acoplar " +
                    "a camada de apresentacao ao detalhe da navegacao.",
                status = TaskStatus.Ready,
            ),
            onBack = { true },
        )
    }
}

@Composable
private fun TaskStatus.labelRes(): Int = when (this) {
    TaskStatus.Ready -> R.string.task_status_ready
    TaskStatus.InProgress -> R.string.task_status_in_progress
    TaskStatus.Blocked -> R.string.task_status_blocked
}
