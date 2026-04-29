package com.valmiraguiar.listo.feature.tasks.domain.usecase

import com.valmiraguiar.listo.feature.tasks.domain.model.Task
import com.valmiraguiar.listo.feature.tasks.domain.repository.TaskRepository
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
) {
    operator fun invoke(): List<Task> = taskRepository.getTasks()
}
