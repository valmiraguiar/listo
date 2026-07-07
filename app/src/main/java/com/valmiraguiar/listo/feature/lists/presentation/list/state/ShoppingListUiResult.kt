package com.valmiraguiar.listo.feature.lists.presentation.list.state

sealed interface ShoppingListUiResult {
    data object OnLoading : ShoppingListUiResult
    data object OnShowLists : ShoppingListUiResult
    data object OnShowEmptyLists : ShoppingListUiResult
    data object OnError : ShoppingListUiResult
}