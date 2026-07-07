package com.valmiraguiar.listo.feature.lists.presentation.list.state

sealed interface ShoppingListsUiAction {
    data object FetchLists : ShoppingListsUiAction
    data class ListCardClick(val cardId: Long) : ShoppingListsUiAction
    data object BackClick : ShoppingListsUiAction
    data object NewListClick : ShoppingListsUiAction
}