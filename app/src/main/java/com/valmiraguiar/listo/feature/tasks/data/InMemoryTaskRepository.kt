package com.valmiraguiar.listo.feature.tasks.data

import android.content.Context
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.tasks.domain.model.Task
import com.valmiraguiar.listo.feature.tasks.domain.model.TaskStatus
import com.valmiraguiar.listo.feature.tasks.domain.repository.TaskRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTaskRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : TaskRepository {

    override fun getTasks(): List<Task> = listOf(
        Task(
            id = 1L,
            title = context.getString(R.string.task_1_title),
            summary = context.getString(R.string.task_1_summary),
            description = context.getString(R.string.task_1_description),
            status = TaskStatus.Ready,
        ),
        Task(
            id = 2L,
            title = context.getString(R.string.task_2_title),
            summary = context.getString(R.string.task_2_summary),
            description = context.getString(R.string.task_2_description),
            status = TaskStatus.InProgress,
        ),
        Task(
            id = 3L,
            title = context.getString(R.string.task_3_title),
            summary = context.getString(R.string.task_3_summary),
            description = context.getString(R.string.task_3_description),
            status = TaskStatus.Blocked,
        ),
    )

    override fun getTaskById(taskId: Long): Task? = getTasks().firstOrNull { it.id == taskId }
}
