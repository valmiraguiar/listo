package com.valmiraguiar.listo.feature.lists.presentation.create

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.lists.domain.model.ListCategory
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraftItem
import com.valmiraguiar.listo.feature.lists.domain.model.UnitOption
import com.valmiraguiar.listo.feature.lists.domain.usecase.CreateShoppingListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateListViewModel @Inject constructor(
    private val createShoppingListUseCase: CreateShoppingListUseCase,
) : ViewModel() {

    private var nextItemId = 1L

    private val _uiState = MutableStateFlow(CreateListUiState(items = listOf(newDraftItem())))
    val uiState: StateFlow<CreateListUiState> = _uiState.asStateFlow()

    fun updateListTitle(title: String) {
        _uiState.update { current ->
            current.copy(listTitle = title)
        }
    }

    fun updateItemQuantity(itemId: Long, quantity: String) {
        updateItem(itemId) { item -> item.copy(quantity = quantity) }
    }

    fun updateItemUnit(itemId: Long, unit: UnitOption) {
        updateItem(itemId) { item -> item.copy(unit = unit) }
    }

    fun updateItemDescription(itemId: Long, description: String) {
        updateItem(itemId) { item -> item.copy(description = description) }
    }

    fun updateItemCategory(itemId: Long, category: ListCategory) {
        updateItem(itemId) { item -> item.copy(category = category) }
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

    fun createList() {
        val snapshot = _uiState.value
        val filteredItems = snapshot.items.filter { it.description.isNotBlank() }
        if (snapshot.listTitle.isBlank() || filteredItems.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(isSaving = true)
            }

            createShoppingListUseCase(
                ShoppingListDraft(
                    title = snapshot.listTitle.trim(),
                    items = filteredItems.map { item ->
                        ShoppingListDraftItem(
                            quantity = item.quantity.trim(),
                            unit = item.unit,
                            description = item.description.trim(),
                            category = item.category,
                        )
                    },
                ),
            )

            _uiState.value = CreateListUiState(
                items = listOf(newDraftItem()),
                saveConfirmationVisible = true,
            )
        }
    }

    fun dismissSaveConfirmation() {
        _uiState.update { current ->
            current.copy(saveConfirmationVisible = false)
        }
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

    private fun newDraftItem(): DraftListItemUiState {
        val id = nextItemId
        nextItemId += 1
        return DraftListItemUiState(
            id = id,
            quantity = "",
            unit = UnitOption.Unit,
            description = "",
            category = ListCategory.Grocery,
        )
    }
}
