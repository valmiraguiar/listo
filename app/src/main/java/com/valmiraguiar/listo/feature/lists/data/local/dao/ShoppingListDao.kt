package com.valmiraguiar.listo.feature.lists.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListWithProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Transaction
    @Query(
        """
            SELECT * FROM shopping_lists ORDER BY created_at DESC
        """
    )
    fun observeAll(): Flow<List<ShoppingListWithProducts>>

    @Transaction
    @Query(
        """
        SELECT * FROM shopping_lists WHERE list_id = :listId LIMIT 1
    """
    )
    fun observeById(listId: Long): Flow<ShoppingListWithProducts?>

    @Transaction
    @Query(
        """
            SELECT * FROM shopping_lists WHERE list_id = :listId LIMIT 1
        """
    )
    suspend fun getById(
        listId: Long
    ): ShoppingListWithProducts?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(
        shoppingList: ShoppingListEntity
    ): Long

    @Update
    suspend fun update(shoppingList: ShoppingListEntity): Int

    @Query(
        """
            UPDATE shopping_lists SET title = :title WHERE list_id = :shoppingListId
        """
    )
    suspend fun updateTitle(
        shoppingListId: Long,
        title: String
    ): Int

    @Delete
    suspend fun delete(
        shoppingList: ShoppingListEntity
    )

    @Query(
        """
            DELETE FROM shopping_lists WHERE list_id = :listId
        """
    )
    suspend fun deleteById(
        listId: Long
    ): Int
}