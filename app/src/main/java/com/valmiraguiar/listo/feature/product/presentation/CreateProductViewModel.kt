package com.valmiraguiar.listo.feature.product.presentation

import androidx.lifecycle.ViewModel
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.plus

class CreateProductViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(
        CreateProductUiState(items = listOf(newDraftItem()))
    )
    val uiState: StateFlow<CreateProductUiState> = _uiState.asStateFlow()

    private var nextItemId = 1L

    private fun newDraftItem(): DraftListItemUiState {
        val id = nextItemId
        nextItemId += 1
        return DraftListItemUiState(
            id = id,
            quantity = "",
            unit = UnitEnum.Unit,
            description = "",
            categoryEnum = CategoryEnum.fromId(1),
        )
    }

    private fun updateItem(
        itemId: Long,
        transform: (DraftListItemUiState) -> DraftListItemUiState,
    ) {
        _uiState.update { current ->
            current.copy(
                items = current.items.map { item ->
                    if (item.id == itemId) transform(item) else item
                },
            )
        }
    }

    fun updateItemQuantity(itemId: Long, quantity: String) {
        updateItem(itemId) { item -> item.copy(quantity = quantity) }
    }

    fun updateItemUnit(itemId: Long, unit: UnitEnum) {
        updateItem(itemId) { item -> item.copy(unit = unit) }
    }

    fun updateItemDescription(itemId: Long, description: String) {
        updateItem(itemId) { item -> item.copy(description = description) }
    }

    fun updateItemCategory(itemId: Long, categoryEnum: CategoryEnum) {
        updateItem(itemId) { item -> item.copy(categoryEnum = categoryEnum) }
    }

    fun addItem() {
        _uiState.update { current ->
            current.copy(items = current.items + newDraftItem())
        }
    }

    fun removeItem(itemId: Long) {
        _uiState.update { current ->
            current.copy(items = current.items.filter { item ->
                item.id != itemId
            })
        }
    }
}