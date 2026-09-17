package com.valmiraguiar.listo.feature.login.di

import com.valmiraguiar.listo.feature.login.data.repository.AuthRepositoryImpl
import com.valmiraguiar.listo.feature.login.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        implementation: AuthRepositoryImpl,
    ): AuthRepository
}
