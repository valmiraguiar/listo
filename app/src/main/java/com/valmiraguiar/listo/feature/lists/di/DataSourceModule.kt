package com.valmiraguiar.listo.feature.lists.di

import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSource
import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindShoppingListLocalDataSource(
        implementation: ShoppingListLocalDataSourceImpl
    ): ShoppingListLocalDataSource
}