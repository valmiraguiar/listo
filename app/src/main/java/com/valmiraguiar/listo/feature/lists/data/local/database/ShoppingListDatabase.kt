package com.valmiraguiar.listo.feature.lists.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.valmiraguiar.listo.feature.lists.data.local.dao.ProductDao
import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity

@Database(
    entities = [
        ShoppingListEntity::class,
        ProductEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters
abstract class ShoppingListDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
    abstract fun productDao(): ProductDao
}
