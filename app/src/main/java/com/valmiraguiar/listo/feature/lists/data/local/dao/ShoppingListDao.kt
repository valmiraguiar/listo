package com.valmiraguiar.listo.feature.lists.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.valmiraguiar.listo.feature.lists.data.local.entity.CategoryEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductWithCategoryEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingListEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingListProduct(ref: List<ShoppingListProductEntity>)

    @Query(
        """
        SELECT *
        FROM shopping_lists
        ORDER BY created_at DESC
        """,
    )
    fun observeShoppingLists(): Flow<List<ShoppingListEntity>>

    @Query(
        """
        SELECT *
        FROM shopping_lists
        WHERE list_id = :listId
        """,
    )
    fun observeShoppingListById(listId: Long): Flow<ShoppingListEntity?>

    @Transaction
    suspend fun insertShoppingListWithItems(
        list: ShoppingListEntity,
        items: List<ProductEntity>,
    ): Long {
        val listId = insertList(list)
        val productIds = insertProducts(items)
        insertShoppingListProduct(
            ref = productIds.map { productId ->
                ShoppingListProductEntity(
                    listId = listId,
                    productId = productId,
                )
            },
        )
        return listId
    }

    @Query(
        """
        SELECT
            p.product_id as product_id,
            p.product_name as product_name,
            p.quantity as quantity,
            p.unit as unit,
            c.category_id as category_id,
            c.category_name as category_name
        FROM shopping_list_product ref
        JOIN products p ON p.product_id = ref.product_id
        JOIN category c ON c.category_id = p.category_id
        WHERE ref.list_id = :listId
        ORDER BY p.product_id ASC
        """
    )
    suspend fun getProductsWithCategoryByList(
        listId: Long
    ): List<ProductWithCategoryEntity>

    @Query(
        """
        SELECT
            p.product_id as product_id,
            p.product_name as product_name,
            p.quantity as quantity,
            p.unit as unit,
            c.category_id as category_id,
            c.category_name as category_name
        FROM shopping_list_product ref
        JOIN products p ON p.product_id = ref.product_id
        JOIN category c ON c.category_id = p.category_id
        WHERE ref.list_id = :listId
        ORDER BY p.product_id ASC
    """
    )
    fun observeProductsWithCategoryByList(
        listId: Long
    ): Flow<List<ProductWithCategoryEntity>>
}
