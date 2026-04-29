package com.valmiraguiar.listo.feature.tasks.domain.repository

import com.valmiraguiar.listo.feature.tasks.domain.model.Task

interface TaskRepository {
    fun getTasks(): List<Task>

    fun getTaskById(taskId: Long): Task?
}
