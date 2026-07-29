package com.valmiraguiar.listo.feature.lists.presentation.details.state

sealed interface ShoppingListDetailsUiResult {
    data object OnLoading : ShoppingListDetailsUiResult
    data object OnShowListDetails : ShoppingListDetailsUiResult
    data object OnShowListNotFound : ShoppingListDetailsUiResult
    data object OnError : ShoppingListDetailsUiResult
    data object OnEditListNavigate : ShoppingListDetailsUiResult
    data object OnNavigateBack : ShoppingListDetailsUiResult
}
