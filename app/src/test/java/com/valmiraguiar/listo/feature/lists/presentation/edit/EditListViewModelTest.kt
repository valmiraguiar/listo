package com.valmiraguiar.listo.feature.lists.presentation.edit

import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.lists.domain.repository.ShoppingListRepository
import com.valmiraguiar.listo.feature.lists.domain.usecase.CreateShoppingListUseCase
import com.valmiraguiar.listo.feature.lists.domain.usecase.ObserveShoppingListDetailsUseCase
import com.valmiraguiar.listo.feature.lists.domain.usecase.UpdateShoppingListUseCase
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class EditListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `new items use temporary ids and are saved with generated ids`() = runTest(
        mainDispatcherRule.testDispatcher,
    ) {
        val repository = FakeShoppingListRepository(
            shoppingList = ShoppingList(
                id = 10L,
                title = "Feira",
                products = listOf(
                    Product(
                        id = 1L,
                        description = "Arroz",
                        quantity = "2",
                        unit = UnitEnum.Unit,
                        category = CategoryEnum.Grocery,
                    ),
                ),
                createdAt = 1L,
            ),
        )
        val viewModel = EditListViewModel(
            createShoppingListUseCase = CreateShoppingListUseCase(repository),
            observeShoppingListDetailsUseCase = ObserveShoppingListDetailsUseCase(repository),
            updateShoppingListUseCase = UpdateShoppingListUseCase(repository),
        )

        viewModel.dispatch(EditListUiAction.OpenList(listId = 10L))
        advanceUntilIdle()
        viewModel.dispatch(EditListUiAction.AddItemClick)
        advanceUntilIdle()

        val temporaryItem = viewModel.uiState.value.shoppingList?.products?.last()
        assertNotNull(temporaryItem)
        checkNotNull(temporaryItem)
        assertTrue(temporaryItem.id < 0L)

        viewModel.dispatch(
            EditListUiAction.ItemDescriptionChange(
                itemId = temporaryItem.id,
                description = "Banana",
            ),
        )
        viewModel.dispatch(EditListUiAction.SaveListClick)
        advanceUntilIdle()

        val savedList = repository.updatedShoppingList
        assertNotNull(savedList)
        checkNotNull(savedList)
        assertEquals(listOf(1L, 0L), savedList.products.map { product -> product.id })
        assertEquals("Banana", savedList.products.last().description)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

private class FakeShoppingListRepository(
    private val shoppingList: ShoppingList?,
) : ShoppingListRepository {
    var updatedShoppingList: ShoppingList? = null
        private set

    override fun observeShoppingLists(): Flow<List<ShoppingList>> = flowOf(emptyList())

    override suspend fun createShoppingList(shoppingList: ShoppingList) = Unit

    override fun observeShoppingListDetails(listId: Long): Flow<ShoppingList?> {
        return flowOf(shoppingList)
    }

    override suspend fun updateShoppingList(shoppingList: ShoppingList) {
        updatedShoppingList = shoppingList
    }

    override suspend fun updateProductsCheckedState(
        shoppingListId: Long,
        checkedProductIds: Set<Long>,
    ) = Unit

    override suspend fun deleteShoppingList(listId: Long) = Unit
}
