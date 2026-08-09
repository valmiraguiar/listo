package com.valmiraguiar.listo.feature.lists.presentation.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.common.flow.FlowResult
import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.lists.domain.usecase.CreateShoppingListUseCase
import com.valmiraguiar.listo.feature.lists.domain.usecase.ObserveShoppingListDetailsUseCase
import com.valmiraguiar.listo.feature.lists.domain.usecase.UpdateShoppingListUseCase
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiAction
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditListViewModel @Inject constructor(
    private val createShoppingListUseCase: CreateShoppingListUseCase,
    private val observeShoppingListDetailsUseCase: ObserveShoppingListDetailsUseCase,
    private val updateShoppingListUseCase: UpdateShoppingListUseCase,
) : ViewModel() {
    private var nextItemId = FIRST_ITEM_ID
    private var observeListJob: Job? = null

    private val _uiState = MutableStateFlow(initialUiState())
    val uiState: StateFlow<EditListUiState> = _uiState.asStateFlow()

    private val _uiResult = MutableSharedFlow<EditListUiResult>()
    val uiResult: SharedFlow<EditListUiResult> get() = _uiResult

    fun dispatch(action: EditListUiAction) {
        when (action) {
            is EditListUiAction.OpenList -> openList(action.listId)
            is EditListUiAction.AddItemClick -> addItem()
            is EditListUiAction.BackClick -> emitUiResult(EditListUiResult.OnNavigateBack)
            is EditListUiAction.ListNameChange -> updateListName(action.listName)
            is EditListUiAction.ItemCategoryChange -> updateItem(action.itemId) { item ->
                item.copy(category = action.categoryEnum)
            }

            is EditListUiAction.ItemDescriptionChange -> updateItem(action.itemId) { item ->
                item.copy(description = action.description)
            }

            is EditListUiAction.ItemQuantityChange -> updateItem(action.itemId) { item ->
                item.copy(quantity = action.quantity)
            }

            is EditListUiAction.RemoveItemClick -> removeItem(action.itemId)
            is EditListUiAction.SaveListClick -> saveList()
        }
    }

    private fun initialUiState(): EditListUiState = EditListUiState(
        shoppingList = ShoppingList(
            products = listOf(newItem()),
            title = "",
            createdAt = 0L,
            id = 0L
        )
    )

    private fun openList(listId: Long?) {
        observeListJob?.cancel()

        if (listId == null) {
            nextItemId = FIRST_ITEM_ID
            _uiState.value = initialUiState()
            return
        }

        observeListJob = viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isSaving = false,
                    editingListId = listId,
                )
            }

            observeShoppingListDetailsUseCase(listId).collect { result ->
                when (result) {
                    is FlowResult.Success -> handleListDetailsLoaded(
                        shoppingList = result.data,
                        listId = listId,
                    )

                    is FlowResult.Error -> handleListDetailsLoadError()
                }
            }
        }
    }

    private fun handleListDetailsLoaded(
        shoppingList: ShoppingList?,
        listId: Long,
    ) {
        if (shoppingList == null) {
            handleListDetailsLoadError()
            return
        }

        nextItemId = (shoppingList.products.maxOfOrNull { item -> item.id } ?: 0L) + 1

        _uiState.update {
            it.copy(
                isLoading = false,
                isSaving = false,
                editingListId = listId,
                shoppingList = shoppingList,
            )
        }
    }

    private fun handleListDetailsLoadError() {
        _uiState.update { it.copy(isLoading = false, isSaving = false) }
        emitUiResult(EditListUiResult.OnError)
    }

    private fun addItem() {
        _uiState.update { current ->
            current.copy(
                shoppingList = current.shoppingList?.copy(
                    products = current.shoppingList.products + newItem()
                )
            )
        }
    }

    private fun removeItem(itemId: Long) {
        _uiState.update { current ->
            current.copy(
                shoppingList = current.shoppingList?.copy(
                    products = current.shoppingList.products.filterNot { item -> item.id == itemId }
                )
            )
        }
    }

    private fun updateListName(
        listName: String
    ) {
        _uiState.update { current ->
            current.copy(
                shoppingList = current.shoppingList?.copy(
                    title = listName
                )
            )
        }
    }

    private fun updateItem(
        itemId: Long,
        transform: (Product) -> Product,
    ) {
        _uiState.update { current ->
            current.copy(
                shoppingList = current.shoppingList?.copy(
                    products = current.shoppingList.products.map { item ->
                        if (item.id == itemId) transform(item) else item
                    }
                )
            )
        }
    }

    private fun saveList() {
        val currentState = _uiState.value
        if (!currentState.canSave) return

        if (currentState.shoppingList == null) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            runCatching {
                currentState.editingListId?.let {
                    updateShoppingListUseCase(
                        shoppingList = formatToSaveList(currentState.shoppingList),
                    )
                } ?: createShoppingListUseCase(
                    formatToSaveList(currentState.shoppingList)
                )
            }.onSuccess {
                observeListJob?.cancel()
                _uiResult.emit(EditListUiResult.OnListSaved)
                _uiState.update { initialUiState() }
            }.onFailure { cause ->
                Log.e("app", "error cause -> $cause")

                _uiResult.emit(EditListUiResult.OnError)
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun emitUiResult(uiResult: EditListUiResult) = viewModelScope.launch {
        _uiResult.emit(uiResult)
    }

    private fun newItem(): Product {
        val id = nextItemId
        nextItemId += 1
        return Product(
            id = id,
            description = "",
            quantity = "",
            unit = UnitEnum.Unit,
            category = CategoryEnum.Grocery,
        )
    }

    private fun formatToSaveList(shoppingList: ShoppingList): ShoppingList {
        val filteredProductList = shoppingList.products.filter {
            it.description.isNotEmpty()
        }

        return shoppingList.copy(
            products = filteredProductList
        )
    }

    private companion object {
        const val FIRST_ITEM_ID = 1L
    }
}
