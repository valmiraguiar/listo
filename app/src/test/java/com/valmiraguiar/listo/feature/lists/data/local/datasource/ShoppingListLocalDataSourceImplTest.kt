package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.data.local.dao.ProductDao
import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.database.ShoppingListDatabase
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListWithProducts
import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShoppingListLocalDataSourceImplTest {

    private val database = mockk<ShoppingListDatabase>()
    private val shoppingListDao = mockk<ShoppingListDao>()
    private val productDao = mockk<ProductDao>()
    private val localDataSource = ShoppingListLocalDataSourceImpl(
        database = database,
        shoppingListDao = shoppingListDao,
        productDao = productDao,
    )

    @Test
    fun `observeShoppingLists maps local entities to domain models`() = runTest {
        every { shoppingListDao.observeAll() } returns flowOf(
            listOf(
                ShoppingListWithProducts(
                    shoppingList = ShoppingListEntity(
                        id = 15L,
                        title = "Feira do mes",
                        createdAt = 1_800L,
                    ),
                    products = listOf(
                        ProductEntity(
                            id = 30L,
                            shoppingListId = 15L,
                            description = "Tomate",
                            quantity = "2",
                            unit = UnitEnum.Kilogram,
                            category = CategoryEnum.Grocery,
                            isChecked = true,
                        ),
                    ),
                ),
            ),
        )

        val result = localDataSource.observeShoppingLists().first()

        assertEquals(
            listOf(
                ShoppingList(
                    id = 15L,
                    title = "Feira do mes",
                    createdAt = 1_800L,
                    products = listOf(
                        Product(
                            id = 30L,
                            description = "Tomate",
                            quantity = "2",
                            unit = UnitEnum.Kilogram,
                            category = CategoryEnum.Grocery,
                            isChecked = true,
                        ),
                    ),
                ),
            ),
            result,
        )
    }
}
