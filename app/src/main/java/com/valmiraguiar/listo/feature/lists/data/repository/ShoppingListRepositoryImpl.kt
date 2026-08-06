package com.valmiraguiar.listo.feature.lists.data.repository

import com.valmiraguiar.listo.feature.lists.data.local.datasource.ShoppingListLocalDataSource
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListLocalDataSource: ShoppingListLocalDataSource,
) : ShoppingListRepository {
    override fun observeShoppingLists(): Flow<List<ShoppingList>> {
        return shoppingListLocalDataSource.observeShoppingLists()
    }
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