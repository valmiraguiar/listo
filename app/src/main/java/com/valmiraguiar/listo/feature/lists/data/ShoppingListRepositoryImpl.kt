package com.valmiraguiar.listo.feature.lists.data

import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListItemEntity
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import javax.inject.Inject

class ShoppingListRepositoryImpl @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListRepository {
    override suspend fun createShoppingList(draft: ShoppingListDraft): Long {
        return shoppingListDao.insertShoppingListWithItems(
            list = ShoppingListEntity(
                title = draft.title,
                createdAt = System.currentTimeMillis(),
            ),
            items = draft.items.mapIndexed { index, item ->
                ShoppingListItemEntity(
                    listId = 0L,
                    position = index,
                    quantity = item.quantity,
                    unit = item.unit,
                    description = item.description,
                    category = item.category,
                )
            },
        )
    }
}
