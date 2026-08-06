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

    override suspend fun createShoppingList(shoppingList: ShoppingList) {
        shoppingListLocalDataSource.createShoppingList(shoppingList)
    }

    override fun observeShoppingListDetails(listId: Long): Flow<ShoppingList?> {
        return shoppingListLocalDataSource.observeShoppingList(listId)
    }

    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        return shoppingListLocalDataSource.updateShoppingList(shoppingList)
    }
}