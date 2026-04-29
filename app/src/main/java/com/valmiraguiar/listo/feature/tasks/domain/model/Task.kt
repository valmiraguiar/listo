package com.valmiraguiar.listo.feature.tasks.domain.model

data class Task(
    val id: Long,
    val title: String,
    val summary: String,
    val description: String,
    val status: TaskStatus,
)

enum class TaskStatus {
    Ready,
    InProgress,
    Blocked,
}
