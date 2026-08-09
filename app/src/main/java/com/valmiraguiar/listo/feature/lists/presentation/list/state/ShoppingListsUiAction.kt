package com.valmiraguiar.listo.feature.lists.presentation.list.state

sealed interface ShoppingListsUiAction {
    data object FetchLists : ShoppingListsUiAction
    data class ItemListClick(val cardId: Long) : ShoppingListsUiAction
    data class DeleteListClick(val listId: Long) : ShoppingListsUiAction
    data object BackClick : ShoppingListsUiAction
    data object NewListClick : ShoppingListsUiAction
    data object LoginPromptDismiss : ShoppingListsUiAction
    data object LoginPromptLoginClick : ShoppingListsUiAction
}
