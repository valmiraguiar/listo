package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDetails
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListProduct
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShoppingListLocalDataSourceImpl @Inject constructor(
    private val shoppingListDao: ShoppingListDao,
) : ShoppingListLocalDataSource {

    override fun observeShoppingLists(): Flow<List<ShoppingListSummary>> {
        return shoppingListDao.observeShoppingLists().map { entities ->
            entities.map { entity ->
                ShoppingListSummary(
                    id = entity.id,
                    title = entity.title,
                    createdAt = entity.createdAt,
                )
            }
        }
    }

    override fun observeShoppingListDetails(listId: Long): Flow<ShoppingListDetails?> {
        return combine(
            shoppingListDao.observeShoppingListById(listId),
            shoppingListDao.observeProductsWithCategoryByList(listId),
        ) { list, products ->
            list?.let {
                ShoppingListDetails(
                    id = it.id,
                    title = it.title,
                    products = products.map { product ->
                        ShoppingListProduct(
                            id = product.productId,
                            title = product.productName,
                            quantity = product.quantity,
                            unit = product.unit,
                            categoryName = product.categoryName,
                        )
                    },
                )
            }
        }
    }

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
