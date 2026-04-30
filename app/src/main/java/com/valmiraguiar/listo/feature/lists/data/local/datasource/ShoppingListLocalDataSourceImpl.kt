package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import javax.inject.Inject

class ShoppingListLocalDataSourceImpl @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListLocalDataSource {

    override suspend fun createShoppingList(draft: ShoppingListDraft): Long {
        return shoppingListDao.insertShoppingListWithItems(
            list = ShoppingListEntity(
                title = draft.title,
                createdAt = System.currentTimeMillis(),
            ),
            items = draft.items.map { item ->
                ProductEntity(
                    productName = item.description,
                    quantity = item.quantity,
                    unit = item.unit,
                    categoryId = item.categoryEnum.id,
                )
            },
        )
    }
}
