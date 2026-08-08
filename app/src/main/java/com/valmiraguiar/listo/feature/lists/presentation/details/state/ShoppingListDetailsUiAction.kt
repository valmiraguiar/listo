package com.valmiraguiar.listo.feature.lists.presentation.details.state

sealed interface ShoppingListDetailsUiAction {
    data class FetchListDetails(val listId: Long) : ShoppingListDetailsUiAction
    data class ItemCheckedChange(
        val itemId: Long,
        val isChecked: Boolean,
    ) : ShoppingListDetailsUiAction
    data object EditListClick : ShoppingListDetailsUiAction
    data object BackClick : ShoppingListDetailsUiAction
    data object SaveCheckedProducts : ShoppingListDetailsUiAction
}
