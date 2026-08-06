package com.valmiraguiar.listo.feature.lists.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Transaction
    @Query(
        """
            SELECT * FROM products WHERE shopping_list_id = :listId ORDER BY product_name ASC
        """
    )
    fun observeByShoppingListId(
        listId: Long
    ): Flow<List<ProductEntity>>

    @Query(
        """
            SELECT * FROM products WHERE product_id = :productId LIMIT 1
        """
    )
    suspend fun getById(
        productId: Long
    ): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(
        product: ProductEntity
    ): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(
        products: List<ProductEntity>
    ): List<Long>

    @Update
    suspend fun update(
        product: ProductEntity
    ): Int

    @Delete
    suspend fun delete(
        product: ProductEntity
    ): Int

    @Query(
        """
            DELETE FROM products WHERE product_id = :productId AND shopping_list_id = :shoppingListId
        """
    )
    suspend fun deleteById(
        shoppingListId: Long,
        productId: Long
    ): Int

    @Query(
        """
            DELETE FROM products WHERE shopping_list_id = :shoppingListId
        """
    )
    suspend fun deleteByShoppingListId(
        shoppingListId: Long
    ): Int
}