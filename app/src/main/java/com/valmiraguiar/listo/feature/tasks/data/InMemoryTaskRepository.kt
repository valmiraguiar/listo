package com.valmiraguiar.listo.feature.tasks.data

import com.valmiraguiar.listo.feature.tasks.domain.model.Task
import com.valmiraguiar.listo.feature.tasks.domain.model.TaskStatus
import com.valmiraguiar.listo.feature.tasks.domain.repository.TaskRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTaskRepository @Inject constructor() : TaskRepository {

    private val tasks = listOf(
        Task(
            id = 1L,
            title = "Definir stack base",
            summary = "Compose + Navigation 3 + Hilt integrados.",
            description = "A navegação agora usa rotas tipadas, ViewModels por destino " +
                "e back stack controlada pela própria aplicação.",
            status = TaskStatus.Ready,
        ),
        Task(
            id = 2L,
            title = "Organizar features",
            summary = "Entradas de navegação registradas por feature.",
            description = "Cada feature expõe seus destinos para o NavHost sem acoplar " +
                "a UI ao detalhe da implementação da back stack.",
            status = TaskStatus.InProgress,
        ),
        Task(
            id = 3L,
            title = "Preparar evolução",
            summary = "Base pronta para modularização futura.",
            description = "O contrato de repositório e os casos de uso já deixam espaço " +
                "para trocar a fonte de dados sem mexer na camada de apresentação.",
            status = TaskStatus.Blocked,
        ),
    )

    override fun getTasks(): List<Task> = tasks

    override fun getTaskById(taskId: Long): Task? = tasks.firstOrNull { it.id == taskId }
}
