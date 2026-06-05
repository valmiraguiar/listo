package com.valmiraguiar.listo.feature.lists.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraft
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListDraftItem
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

    private val _uiState = MutableStateFlow(CreateListUiState(items = listOf()))
    val uiState: StateFlow<CreateListUiState> = _uiState.asStateFlow()

    fun updateListTitle(title: String) {
        _uiState.update { current ->
            current.copy(listTitle = title)
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
                            categoryEnum = item.categoryEnum,
                        )
                    },
                ),
            )

            _uiState.value = CreateListUiState(
                items = listOf(),
                saveConfirmationVisible = true,
            )
        }
    }

    fun dismissSaveConfirmation() {
        _uiState.update { current ->
            current.copy(saveConfirmationVisible = false)
        }
    }
}
