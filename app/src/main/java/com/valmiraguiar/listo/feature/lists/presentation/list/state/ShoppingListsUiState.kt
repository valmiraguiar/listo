package com.valmiraguiar.listo.feature.lists.presentation.list.state

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList

data class ShoppingListsUiState(
    val isLoading: Boolean = true,
    val shoppingLists: List<ShoppingList> = emptyList(),
)
