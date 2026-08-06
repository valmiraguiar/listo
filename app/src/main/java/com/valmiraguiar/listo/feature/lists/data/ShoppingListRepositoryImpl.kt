package com.valmiraguiar.listo.feature.lists.data

import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSource
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
) : ShoppingListRepository {
//    override fun observeShoppingLists(): Flow<List<ShoppingListSummary>> {
//        return shoppingListLocalDataSource.observeShoppingLists()
//    }
//
//    override fun observeShoppingListDetails(listId: Long): Flow<ShoppingListDetails?> {
//        return shoppingListLocalDataSource.observeShoppingListDetails(listId)
//    }
//
//    override suspend fun createShoppingList(draft: ShoppingListDraft): Long {
//        return shoppingListLocalDataSource.createShoppingList(draft)
//    }
//
//    override suspend fun updateShoppingList(listId: Long, draft: ShoppingListDraft): Long {
//        return shoppingListLocalDataSource.updateShoppingList(listId, draft)
//    }
}
