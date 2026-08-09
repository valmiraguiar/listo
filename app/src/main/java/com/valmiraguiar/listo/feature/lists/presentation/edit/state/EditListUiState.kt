package com.valmiraguiar.listo.feature.lists.presentation.edit.state

import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList

data class EditListUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val editingListId: Long? = null,
    val shoppingList: ShoppingList? = null,
) {
    val canSave: Boolean
        get() = shoppingList?.products?.any { item -> item.description.isNotBlank() } == true &&
            !isLoading &&
            !isSaving
}
