package com.valmiraguiar.listo.feature.lists.data

import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSource
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
) : ShoppingListRepository {
    override suspend fun createShoppingList(draft: ShoppingListDraft): Long {
        return shoppingListLocalDataSource.createShoppingList(draft)
    }
}
