package com.valmiraguiar.listo.feature.lists.presentation.edit.state

import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum

sealed interface EditListUiAction {
    data object AddItemClick : EditListUiAction
    data object SaveListClick : EditListUiAction
    data object BackClick : EditListUiAction
    data class ListNameChange(
        val listName: String,
    ) : EditListUiAction
    data class ItemDescriptionChange(
        val itemId: Long,
        val description: String,
    ) : EditListUiAction
    data class ItemCategoryChange(
        val itemId: Long,
        val categoryEnum: CategoryEnum,
    ) : EditListUiAction
    data class RemoveItemClick(
        val itemId: Long,
    ) : EditListUiAction
}
