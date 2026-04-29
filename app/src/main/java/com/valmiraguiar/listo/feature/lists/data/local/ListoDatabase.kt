package com.valmiraguiar.listo.feature.lists.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.valmiraguiar.listo.feature.lists.data.local.converter.ListTypeConverters
import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListItemEntity

@Database(
    entities = [
        ShoppingListEntity::class,
        ShoppingListItemEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ListTypeConverters::class)
abstract class ListoDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao
}
