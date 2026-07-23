package com.valmiraguiar.listo.feature.lists.presentation.edit.state

sealed interface EditListUiResult {
    data object OnNavigateBack : EditListUiResult
    data class OnListSaved(val listId: Long) : EditListUiResult
    data object OnError : EditListUiResult
}
