package com.valmiraguiar.listo.feature.lists.domain.repository

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft

interface ShoppingListRepository {
    suspend fun createShoppingList(draft: ShoppingListDraft): Long
}
