package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDetails
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import kotlinx.coroutines.flow.Flow

interface ShoppingListLocalDataSource {
    fun observeShoppingLists(): Flow<List<ShoppingListSummary>>

    fun observeShoppingListDetails(listId: Long): Flow<ShoppingListDetails?>

    suspend fun createShoppingList(draft: ShoppingListDraft): Long
}
