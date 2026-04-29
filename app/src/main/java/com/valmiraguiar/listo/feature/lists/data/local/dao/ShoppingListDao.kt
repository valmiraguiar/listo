package com.valmiraguiar.listo.feature.lists.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListItemEntity

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingList(entity: ShoppingListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListItems(entities: List<ShoppingListItemEntity>)

    @Transaction
    suspend fun insertShoppingListWithItems(
        list: ShoppingListEntity,
        items: List<ShoppingListItemEntity>,
    ): Long {
        val listId = insertShoppingList(list)
        insertShoppingListItems(
            entities = items.map { item ->
                item.copy(listId = listId)
            },
        )
        return listId
    }
}
