package com.valmiraguiar.listo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.valmiraguiar.listo.feature.lists.data.local.converter.ListTypeConverters
import com.valmiraguiar.listo.feature.lists.data.local.dao.CategoryDao
import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.CategoryEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListProductEntity

@Database(
    entities = [
        ShoppingListEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        ShoppingListProductEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(ListTypeConverters::class)
abstract class ListoDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun shoppingListDao(): ShoppingListDao
}
