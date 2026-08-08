package com.valmiraguiar.listo.feature.lists.presentation.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.core.di.ApplicationScope
import com.valmiraguiar.listo.feature.common.extensions.onError
import com.valmiraguiar.listo.feature.common.extensions.onSuccess
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList
import com.valmiraguiar.listo.feature.lists.domain.usecase.ObserveShoppingListDetailsUseCase
import com.valmiraguiar.listo.feature.lists.domain.usecase.UpdateProductsCheckedStateUseCase
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsItemUiState
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsUiAction
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsUiResult
import com.valmiraguiar.listo.feature.lists.presentation.details.state.ShoppingListDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListDetailsViewModel @Inject constructor(
    private val observeShoppingListDetailsUseCase: ObserveShoppingListDetailsUseCase,
    private val updateProductsCheckedStateUseCase: UpdateProductsCheckedStateUseCase,
    @ApplicationScope private val applicationScope: CoroutineScope,
) : ViewModel() {
    private var observeDetailsJob: Job? = null
    private var saveCheckedProductsJob: Job? = null
    private var lastSavedCheckedProductIds: Set<Long> = emptySet()

    private val _uiState = MutableStateFlow(ShoppingListDetailsUiState())
    val uiState: StateFlow<ShoppingListDetailsUiState> = _uiState.asStateFlow()

    private val _uiResult = MutableSharedFlow<ShoppingListDetailsUiResult>()
    val uiResult: SharedFlow<ShoppingListDetailsUiResult> get() = _uiResult

    fun dispatch(action: ShoppingListDetailsUiAction) {
        when (action) {
            is ShoppingListDetailsUiAction.FetchListDetails -> observeShoppingListDetails(action.listId)
            is ShoppingListDetailsUiAction.ItemCheckedChange -> updateItemChecked(
                itemId = action.itemId,
                isChecked = action.isChecked,
            )

            is ShoppingListDetailsUiAction.EditListClick -> emitUiResult(
                ShoppingListDetailsUiResult.OnEditListNavigate
            )

            is ShoppingListDetailsUiAction.BackClick -> emitUiResult(
                ShoppingListDetailsUiResult.OnNavigateBack
            )

            is ShoppingListDetailsUiAction.SaveCheckedProducts -> saveCheckedProducts()
        }
    }

    private fun observeShoppingListDetails(listId: Long) {
        observeDetailsJob?.cancel()
        observeDetailsJob = observeShoppingListDetailsUseCase(listId).onStart {
            updateUiState {
                copy(
                    isLoading = true,
                    listId = listId,
                    isNotFound = false,
                )
            }
            emitUiResult(ShoppingListDetailsUiResult.OnLoading)
        }.onCompletion {
            updateUiState { copy(isLoading = false) }
        }.onSuccess { shoppingList ->
            handleShoppingListDetailsSuccess(
                listId = listId,
                shoppingList = shoppingList,
            )
        }.onError { error ->
            handleError(error)
        }.launchIn(viewModelScope)
    }

    private fun handleShoppingListDetailsSuccess(
        listId: Long,
        shoppingList: ShoppingList?,
    ) {
        if (shoppingList == null) {
            updateUiState {
                copy(
                    isLoading = false,
                    listId = listId,
                    title = "",
                    items = emptyList(),
                    isNotFound = true,
                )
            }
            emitUiResult(ShoppingListDetailsUiResult.OnShowListNotFound)
            return
        }

        val checkedItems = uiState.value.items.associate { item -> item.id to item.isChecked }
        val items = shoppingList.products.map { product ->
            ShoppingListDetailsItemUiState(
                id = product.id,
                title = product.description,
                classification = product.category.name,
                quantity = product.quantity,
                unit = product.unit,
                isChecked = checkedItems[product.id] ?: product.isChecked,
            )
        }.sortedBy { item -> item.isChecked }

        updateUiState {
            copy(
                isLoading = false,
                listId = shoppingList.id,
                title = shoppingList.title,
                items = items,
                isNotFound = false,
            )
        }
        lastSavedCheckedProductIds = shoppingList.products
            .filter { product -> product.isChecked }
            .map { product -> product.id }
            .toSet()
        emitUiResult(ShoppingListDetailsUiResult.OnShowListDetails)
    }

    private fun updateItemChecked(
        itemId: Long,
        isChecked: Boolean,
    ) {
        _uiState.update { current ->
            current.copy(
                items = current.items.map { item ->
                    if (item.id == itemId) {
                        item.copy(isChecked = isChecked)
                    } else {
                        item
                    }
                }.sortedBy { item -> item.isChecked },
            )
        }
        saveCheckedProducts()
    }

    private fun saveCheckedProducts() {
        val currentState = uiState.value
        if (
            currentState.listId <= ZERO ||
            currentState.isLoading ||
            currentState.isNotFound
        ) return

        val checkedProductIds = currentState.items.checkedProductIds()
        if (checkedProductIds == lastSavedCheckedProductIds) return

        saveCheckedProductsJob?.cancel()
        saveCheckedProductsJob = applicationScope.launch {
            runCatching {
                updateProductsCheckedStateUseCase(
                    shoppingListId = currentState.listId,
                    checkedProductIds = checkedProductIds,
                )
            }.onSuccess {
                lastSavedCheckedProductIds = checkedProductIds
            }.onFailure { error ->
                Log.e("app-error-log", error.toString())
            }
        }
    }

    private fun handleError(error: Throwable) {
        Log.e("app-error-log", error.toString())
        updateUiState { copy(isLoading = false) }
        emitUiResult(ShoppingListDetailsUiResult.OnError)
    }

    private fun emitUiResult(uiResult: ShoppingListDetailsUiResult) = viewModelScope.launch {
        _uiResult.emit(uiResult)
    }

    private fun updateUiState(
        reduce: ShoppingListDetailsUiState.() -> ShoppingListDetailsUiState,
    ) {
        _uiState.value = uiState.value.reduce()
    }

    private fun List<ShoppingListDetailsItemUiState>.checkedProductIds(): Set<Long> {
        return filter { item -> item.isChecked }
            .map { item -> item.id }
            .toSet()
    }

    private companion object {
        const val ZERO = 0
    }
}
