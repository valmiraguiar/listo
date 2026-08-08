package com.valmiraguiar.listo.feature.lists.domain.repository

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeShoppingLists(): Flow<List<ShoppingList>>
    suspend fun createShoppingList(shoppingList: ShoppingList)
    fun observeShoppingListDetails(listId: Long): Flow<ShoppingList?>
    suspend fun updateShoppingList(shoppingList: ShoppingList)
    suspend fun updateProductsCheckedState(shoppingListId: Long, checkedProductIds: Set<Long>)
    suspend fun deleteShoppingList(listId: Long)
}
