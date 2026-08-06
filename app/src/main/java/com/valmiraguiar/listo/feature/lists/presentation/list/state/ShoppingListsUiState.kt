package com.valmiraguiar.listo.feature.lists.presentation.list.state

data class ShoppingListsUiState(
    val isLoading: Boolean = true,
    val shoppingLists: List<Any> = emptyList(),
)
