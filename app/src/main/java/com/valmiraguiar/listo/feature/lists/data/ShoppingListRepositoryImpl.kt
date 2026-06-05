package com.valmiraguiar.listo.feature.lists.data

import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSource
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
) : ShoppingListRepository {
    override fun observeShoppingLists(): Flow<List<ShoppingListSummary>> {
        return shoppingListLocalDataSource.observeShoppingLists()
    }

    override suspend fun createShoppingList(draft: ShoppingListDraft): Long {
        return shoppingListLocalDataSource.createShoppingList(draft)
    }
}
