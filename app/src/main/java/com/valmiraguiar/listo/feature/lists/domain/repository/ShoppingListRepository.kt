package com.valmiraguiar.listo.feature.lists.domain.repository

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeShoppingLists(): Flow<List<ShoppingListSummary>>

    suspend fun createShoppingList(draft: ShoppingListDraft): Long
}
