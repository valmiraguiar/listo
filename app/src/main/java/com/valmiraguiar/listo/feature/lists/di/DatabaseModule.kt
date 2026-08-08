package com.valmiraguiar.listo.feature.lists.di

import android.content.Context
import androidx.room.Room
import com.valmiraguiar.listo.feature.lists.data.local.dao.ProductDao
import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.database.ShoppingListDatabase
import com.valmiraguiar.listo.feature.lists.data.local.database.ShoppingListDatabaseMigrations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideShoppingListDatabase(
        @ApplicationContext context: Context
    ): ShoppingListDatabase {
        return Room.databaseBuilder(
            context,
            ShoppingListDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(
            ShoppingListDatabaseMigrations.MIGRATION_1_2,
        ).build()
    }

    @Provides
    fun provideShoppingListDao(
        database: ShoppingListDatabase
    ): ShoppingListDao = database.shoppingListDao()

    @Provides
    fun provideProductDao(
        database: ShoppingListDatabase
    ): ProductDao = database.productDao()

    private const val DATABASE_NAME = "shopping-list.db"
}
