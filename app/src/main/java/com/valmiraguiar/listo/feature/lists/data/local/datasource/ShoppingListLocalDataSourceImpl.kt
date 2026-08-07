package com.valmiraguiar.listo.feature.lists.data.local.datasource

import androidx.room.withTransaction
import com.valmiraguiar.listo.R
import com.valmiraguiar.listo.feature.lists.data.local.converter.toDomain
import com.valmiraguiar.listo.feature.lists.data.local.converter.toEntity
import com.valmiraguiar.listo.feature.lists.data.local.dao.ProductDao
import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.database.ShoppingListDatabase
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShoppingListLocalDataSourceImpl @Inject constructor(
    private val database: ShoppingListDatabase,
    private val shoppingListDao: ShoppingListDao,
    private val productDao: ProductDao
) : ShoppingListLocalDataSource {
    override fun observeShoppingLists(): Flow<List<ShoppingList>> {
        return shoppingListDao.observeAll().map { shoppingLists ->
            shoppingLists.map { shoppingList ->
                shoppingList.toDomain()
            }
        }
    }

    override fun observeShoppingList(listId: Long): Flow<ShoppingList?> {
        return shoppingListDao
            .observeById(listId)
            .map { groceryList ->
                groceryList?.toDomain()
            }
    }

    override suspend fun getShoppingList(listId: Long): ShoppingList? {
        return shoppingListDao
            .getById(listId)
            ?.toDomain()
    }

    override fun observeProducts(listId: Long): Flow<List<Product>> {
        return productDao
            .observeByShoppingListId(listId)
            .map { products ->
                products.map { product ->
                    product.toDomain()
                }
            }
    }

    override suspend fun getProduct(productId: Long): Product? {
        return productDao
            .getById(productId)
            ?.toDomain()
    }

    override suspend fun createShoppingList(shoppingList: ShoppingList) {
        require(shoppingList.title.isNotBlank()) {
            R.string.shopping_list_data_source_invalid_title
        }

        database.withTransaction {
            val createdListId = shoppingListDao.insert(
                ShoppingListEntity(
                    title = shoppingList.title.trim(),
                    createdAt = System.currentTimeMillis()
                ),
            )

            check(createdListId > ZERO) {
                "${R.string.shopping_list_data_source_list_not_found}: ${shoppingList.id}"
            }

            shoppingList.products.forEach { product ->
                productDao.insert(
                    ProductEntity(
                        shoppingListId = createdListId,
                        description = product.description,
                        quantity = product.quantity,
                        unit = product.unit,
                        category = product.category,
                    )
                )
            }
        }
    }

    override suspend fun updateShoppingListTitle(shoppingListId: Long, title: String) {
        require(shoppingListId > ZERO) {
            R.string.shopping_list_data_source_invalid_id
        }

        require(title.isNotBlank()) {
            R.string.shopping_list_data_source_invalid_title
        }

        val updatedRows = shoppingListDao.updateTitle(
            shoppingListId = shoppingListId,
            title = title.trim(),
        )

        check(updatedRows == ONE) {
            "${R.string.shopping_list_data_source_list_not_found}: $shoppingListId"
        }
    }


    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        require(shoppingList.id > ZERO) {
            R.string.shopping_list_data_source_invalid_id
        }

        require(shoppingList.title.isNotBlank()) {
            R.string.shopping_list_data_source_invalid_title
        }

        database.withTransaction {
            val updatedRows = shoppingListDao.update(
                shoppingList.toEntity(),
            )

            check(updatedRows == ONE) {
                "${R.string.shopping_list_data_source_list_not_found}: ${shoppingList.id}"
            }

            productDao.deleteByShoppingListId(
                shoppingListId = shoppingList.id,
            )

            if (shoppingList.products.isNotEmpty()) {
                productDao.insertAll(
                    shoppingList.products.map { product ->
                        product.toEntity(
                            shoppingListId = shoppingList.id,
                        )
                    },
                )
            }
        }
    }

    override suspend fun createProduct(
        shoppingListId: Long,
        product: Product
    ): Long {
        require(shoppingListId > ZERO) {
            R.string.shopping_list_data_source_invalid_id
        }

        require(product.description.isNotBlank()) {
            R.string.shopping_list_data_source_invalid_description
        }

        return productDao.insert(
            product
                .copy(
                    id = 0,
                    description = product.description.trim(),
                )
                .toEntity(
                    shoppingListId = shoppingListId,
                ),
        )
    }

    override suspend fun updateProduct(
        shoppingListId: Long,
        product: Product
    ) {
        require(shoppingListId > ZERO) {
            R.string.shopping_list_data_source_invalid_id
        }

        require(product.id > ZERO) {
            R.string.shopping_list_data_source_invalid_id
        }

        require(product.description.isNotBlank()) {
            R.string.shopping_list_data_source_invalid_description
        }

        val updatedRows = productDao.update(
            product
                .copy(
                    description = product.description.trim(),
                )
                .toEntity(
                    shoppingListId = shoppingListId,
                ),
        )

        check(updatedRows == ONE) {
            "${R.string.shopping_list_data_source_product_not_found}: ${product.id}"
        }
    }

    override suspend fun deleteProduct(shoppingListId: Long, productId: Long) {
        val deletedRows = productDao.deleteById(
            shoppingListId = shoppingListId,
            productId = productId,
        )

        check(deletedRows == ONE) {
            "${R.string.shopping_list_data_source_product_not_found}: $productId"
        }
    }

    override suspend fun deleteShoppingList(shoppingListId: Long) {
        val deletedRows = shoppingListDao.deleteById(
            listId = shoppingListId,
        )

        check(deletedRows == ONE) {
            "${R.string.shopping_list_data_source_list_not_found}: $shoppingListId"
        }
    }

    private companion object {
        const val ZERO = 0
        const val ONE = 1
    }
}