package com.valmiraguiar.listo.feature.login.di

import com.valmiraguiar.listo.feature.login.data.repository.LoginPromptRepositoryImpl
import com.valmiraguiar.listo.feature.login.domain.repository.LoginPromptRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLoginPromptRepository(
        implementation: LoginPromptRepositoryImpl,
    ): LoginPromptRepository
}
