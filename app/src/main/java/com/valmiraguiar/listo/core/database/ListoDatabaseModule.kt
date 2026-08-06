package com.valmiraguiar.listo.core.database

import android.content.Context
import androidx.room.Room
import com.valmiraguiar.listo.feature.lists.data.repository.ShoppingListRepositoryImpl
import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSource
import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSourceImpl
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ListDatabaseModule {
//    @Provides
//    @Singleton
//    fun provideListoDatabase(
//        @ApplicationContext context: Context,
//        databaseCallback: DatabaseCallback,
//    ): ListoDatabase {
//        return Room.databaseBuilder(
//            context = context,
//            klass = ListoDatabase::class.java,
//            name = "listo.db",
//        ).addMigrations(ListoDatabaseMigrations.MIGRATION_1_2)
//            .addCallback(databaseCallback)
//            .build()
//    }
//
//    @Provides
//    fun provideCategoryDao(database: ListoDatabase): CategoryDao {
//        return database.categoryDao()
//    }
//
//    @Provides
//    fun provideShoppingListDao(database: ListoDatabase): ShoppingListDao {
//        return database.shoppingListDao()
//    }
}
