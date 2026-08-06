package com.valmiraguiar.listo.feature.lists.di

import com.valmiraguiar.listo.feature.lists.data.repository.ShoppingListRepositoryImpl
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(
        implementation: ShoppingListRepositoryImpl,
    ): ShoppingListRepository
}