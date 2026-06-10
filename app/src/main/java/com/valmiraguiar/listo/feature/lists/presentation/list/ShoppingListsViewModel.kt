package com.valmiraguiar.listo.feature.lists.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingListSummary
import com.valmiraguiar.listo.feature.lists.domain.usecase.ObserveShoppingListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ShoppingListsViewModel @Inject constructor(
    observeShoppingListsUseCase: ObserveShoppingListsUseCase,
) : ViewModel() {

    val uiState: StateFlow<ShoppingListsUiState> = observeShoppingListsUseCase()
        .map { shoppingLists ->
            ShoppingListsUiState(
                isLoading = false,
                shoppingLists = MOCK.map { shoppingList -> // TODO - Remove mock when database is configured
                    shoppingList.toUiItem()
                },
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ShoppingListsUiState(),
        )

    private fun ShoppingListSummary.toUiItem(): ShoppingListUiItem {
        return ShoppingListUiItem(
            id = id,
            title = title,
            createdAt = createdAt,
        )
    }

    private companion object {
        val MOCK = listOf(
            ShoppingListSummary(
                id = 1L,
                title = "Teste1",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 2L,
                title = "Teste2",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 3L,
                title = "Teste3",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 4L,
                title = "Teste4",
                createdAt = 1_784_324_400_000L
            ),
            ShoppingListSummary(
                id = 5L,
                title = "Teste5",
                createdAt = 1_784_324_400_000L
            )
        )
    }
}
