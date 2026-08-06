package com.valmiraguiar.listo.feature.lists.domain.repository

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeShoppingLists(): Flow<List<ShoppingList>>
//
//    fun observeShoppingListDetails(listId: Long): Flow<ShoppingListDetails?>
//
//    suspend fun createShoppingList(draft: ShoppingListDraft): Long
//
//    suspend fun updateShoppingList(listId: Long, draft: ShoppingListDraft): Long
}
