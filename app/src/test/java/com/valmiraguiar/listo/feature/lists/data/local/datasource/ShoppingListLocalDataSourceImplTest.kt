package com.valmiraguiar.listo.feature.lists.data.local.datasource

import com.valmiraguiar.listo.feature.lists.data.local.dao.ShoppingListDao
import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraftItem
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShoppingListLocalDataSourceImplTest {

    private val shoppingListDao = mockk<ShoppingListDao>()
    private val localDataSource = ShoppingListLocalDataSourceImpl(shoppingListDao)

    @Test
    fun `createShoppingList maps draft into local entities before persisting`() = runTest {
        val listSlot = slot<ShoppingListEntity>()
        val itemsSlot = slot<List<ProductEntity>>()
        val draft = ShoppingListDraft(
            title = "Feira do mes",
            items = listOf(
                ShoppingListDraftItem(
                    quantity = "2",
                    unit = UnitEnum.Kilogram,
                    description = "Tomate",
                    categoryEnum = CategoryEnum.Grocery,
                ),
                ShoppingListDraftItem(
                    quantity = "1",
                    unit = UnitEnum.Unit,
                    description = "Leite",
                    categoryEnum = CategoryEnum.Dairy,
                ),
            ),
        )
        coEvery {
            shoppingListDao.insertShoppingListWithItems(
                list = capture(listSlot),
                items = capture(itemsSlot),
            )
        } returns 15L

        val result = localDataSource.createShoppingList(draft)

        assertEquals(15L, result)
        assertEquals("Feira do mes", listSlot.captured.title)
        assertTrue(listSlot.captured.createdAt > 0L)
        assertEquals(
            listOf(
                ProductEntity(
                    productName = "Tomate",
                    quantity = "2",
                    unit = UnitEnum.Kilogram,
                    categoryId = CategoryEnum.Grocery.id,
                ),
                ProductEntity(
                    productName = "Leite",
                    quantity = "1",
                    unit = UnitEnum.Unit,
                    categoryId = CategoryEnum.Dairy.id,
                ),
            ),
            itemsSlot.captured,
        )
        coVerify(exactly = 1) {
            shoppingListDao.insertShoppingListWithItems(any(), any())
        }
    }
}
