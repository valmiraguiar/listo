package com.valmiraguiar.listo.feature.tasks.presentation.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.valmiraguiar.listo.feature.tasks.domain.model.Task
import com.valmiraguiar.listo.feature.tasks.domain.model.TaskStatus
import com.valmiraguiar.listo.feature.tasks.domain.usecase.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            tasks = getTasksUseCase().map(Task::toUiModel),
        ),
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}

@Immutable
data class HomeUiState(
    val tasks: List<TaskCardUiModel> = emptyList(),
)

@Immutable
data class TaskCardUiModel(
    val id: Long,
    val title: String,
    val summary: String,
    val statusLabel: String,
)

private fun Task.toUiModel(): TaskCardUiModel = TaskCardUiModel(
    id = id,
    title = title,
    summary = summary,
    statusLabel = when (status) {
        TaskStatus.Ready -> "Pronta"
        TaskStatus.InProgress -> "Em andamento"
        TaskStatus.Blocked -> "Bloqueada"
    },
)
