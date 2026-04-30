package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft

interface ShoppingListLocalDataSource {
    suspend fun createShoppingList(draft: ShoppingListDraft): Long
}
