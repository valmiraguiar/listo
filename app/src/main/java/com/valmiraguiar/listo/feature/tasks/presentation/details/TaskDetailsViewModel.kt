package com.valmiraguiar.listo.feature.tasks.presentation.details

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.valmiraguiar.listo.core.navigation.ListoDestination
import com.valmiraguiar.listo.feature.tasks.domain.model.Task
import com.valmiraguiar.listo.feature.tasks.domain.model.TaskStatus
import com.valmiraguiar.listo.feature.tasks.domain.usecase.GetTaskByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = TaskDetailsViewModel.Factory::class)
class TaskDetailsViewModel @AssistedInject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    @Assisted destination: ListoDestination.TaskDetails,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        getTaskByIdUseCase(destination.taskId).toUiState(taskId = destination.taskId),
    )
    val uiState: StateFlow<TaskDetailsUiState> = _uiState.asStateFlow()

    @AssistedFactory
    interface Factory {
        fun create(destination: ListoDestination.TaskDetails): TaskDetailsViewModel
    }
}

@Immutable
data class TaskDetailsUiState(
    val title: String = "",
    val summary: String = "",
    val description: String = "",
    val status: TaskStatus? = null,
    val isNotFound: Boolean = false,
    val missingTaskId: Long? = null,
)

private fun Task?.toUiState(taskId: Long): TaskDetailsUiState {
    if (this == null) {
        return TaskDetailsUiState(
            isNotFound = true,
            missingTaskId = taskId,
        )
    }

    return TaskDetailsUiState(
        title = title,
        summary = summary,
        description = description,
        status = status,
    )
}
