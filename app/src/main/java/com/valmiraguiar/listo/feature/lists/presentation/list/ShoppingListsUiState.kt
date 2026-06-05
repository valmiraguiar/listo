package com.valmiraguiar.listo.feature.lists.presentation.list

import androidx.compose.runtime.Immutable

@Immutable
data class ShoppingListsUiState(
    val isLoading: Boolean = true,
    val shoppingLists: List<ShoppingListUiItem> = emptyList(),
)

@Immutable
data class ShoppingListUiItem(
    val id: Long,
    val title: String,
    val createdAt: Long,
)
