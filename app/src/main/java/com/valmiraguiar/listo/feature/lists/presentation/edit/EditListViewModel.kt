package com.valmiraguiar.listo.feature.lists.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraftItem
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.lists.domain.usecase.CreateShoppingListUseCase
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListItemUiState
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiAction
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiResult
import com.valmiraguiar.listo.feature.lists.presentation.edit.state.EditListUiState
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditListUiState())
    val uiState: StateFlow<EditListUiState> = _uiState.asStateFlow()

    private val _uiResult = MutableSharedFlow<EditListUiResult>()
    val uiResult: SharedFlow<EditListUiResult> get() = _uiResult

    private var nextItemId = FIRST_ITEM_ID

    fun dispatch(action: EditListUiAction) {
        when (action) {
            is EditListUiAction.AddItemClick -> addItem()
            is EditListUiAction.BackClick -> emitUiResult(EditListUiResult.OnNavigateBack)
            is EditListUiAction.ItemCategoryChange -> updateItem(action.itemId) { item ->
                item.copy(categoryEnum = action.categoryEnum)
            }
            is EditListUiAction.ItemDescriptionChange -> updateItem(action.itemId) { item ->
                item.copy(description = action.description)
            }
            is EditListUiAction.SaveListClick -> saveList()
        }
    }

    private fun addItem() {
        _uiState.update { current ->
            current.copy(items = current.items + newItem())
        }
    }

    private fun updateItem(
        itemId: Long,
        transform: (EditListItemUiState) -> EditListItemUiState,
    ) {
        _uiState.update { current ->
            current.copy(
                items = current.items.map { item ->
                    if (item.id == itemId) transform(item) else item
                },
            )
        }
    }

    private fun saveList() {
        val currentState = _uiState.value
        if (!currentState.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            runCatching {
                createShoppingListUseCase(currentState.toDraft())
            }.onSuccess { listId ->
                _uiResult.emit(EditListUiResult.OnListSaved(listId))
                _uiState.update { EditListUiState() }
            }.onFailure {
                _uiResult.emit(EditListUiResult.OnError)
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun emitUiResult(uiResult: EditListUiResult) = viewModelScope.launch {
        _uiResult.emit(uiResult)
    }

    private fun newItem(): EditListItemUiState {
        val id = nextItemId
        nextItemId += 1
        return EditListItemUiState(
            id = id,
            description = "",
            categoryEnum = CategoryEnum.Grocery,
        )
    }

    private fun EditListUiState.toDraft(): ShoppingListDraft {
        val filledItems = items.filter { item -> item.description.isNotBlank() }

        return ShoppingListDraft(
            title = filledItems.firstOrNull()?.description?.trim()?.take(LIST_TITLE_MAX_LENGTH)
                ?: DEFAULT_LIST_TITLE,
            items = filledItems.map { item ->
                ShoppingListDraftItem(
                    quantity = DEFAULT_ITEM_QUANTITY,
                    unit = UnitEnum.Unit,
                    description = item.description.trim(),
                    categoryEnum = item.categoryEnum,
                )
            },
        )
    }

    private companion object {
        const val FIRST_ITEM_ID = 1L
        const val LIST_TITLE_MAX_LENGTH = 40
        const val DEFAULT_ITEM_QUANTITY = ""
        const val DEFAULT_LIST_TITLE = "Nova lista"
    }
}
