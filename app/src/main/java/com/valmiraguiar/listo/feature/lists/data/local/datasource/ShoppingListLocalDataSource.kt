package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListLocalDataSource {
    fun observeShoppingLists(): Flow<List<ShoppingList>>
    fun observeShoppingList(listId: Long): Flow<ShoppingList?>
    suspend fun getShoppingList(listId: Long): ShoppingList?
    fun observeProducts(listId: Long): Flow<List<Product>>
    suspend fun getProduct(productId: Long): Product?
    suspend fun createShoppingList(title: String): Long
    suspend fun updateShoppingListTitle(shoppingListId: Long, title: String)
    suspend fun updateShoppingList(shoppingList: ShoppingList)
    suspend fun createProduct(shoppingListId: Long, product: Product): Long
    suspend fun updateProduct(shoppingListId: Long, product: Product)
    suspend fun deleteProduct(shoppingListId: Long, productId: Long)
    suspend fun deleteShoppingList(shoppingListId: Long)
}
