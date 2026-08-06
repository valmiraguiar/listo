package com.valmiraguiar.listo.feature.lists.presentation.edit.state

sealed interface EditListUiResult {
    data object OnNavigateBack : EditListUiResult
    data object OnListSaved : EditListUiResult
    data object OnError : EditListUiResult
}
