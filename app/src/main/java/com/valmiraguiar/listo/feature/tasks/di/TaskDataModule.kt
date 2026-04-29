package com.valmiraguiar.listo.feature.tasks.di

import com.valmiraguiar.listo.feature.tasks.data.InMemoryTaskRepository
import com.valmiraguiar.listo.feature.tasks.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskDataModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        repository: InMemoryTaskRepository,
    ): TaskRepository
}
