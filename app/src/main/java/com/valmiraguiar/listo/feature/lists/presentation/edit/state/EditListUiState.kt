package com.valmiraguiar.listo.feature.lists.presentation.edit.state

import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum

data class EditListUiState(
    val isSaving: Boolean = false,
    val items: List<EditListItemUiState> = emptyList(),
) {
    val canSave: Boolean
        get() = items.any { item -> item.description.isNotBlank() } && !isSaving
}

data class EditListItemUiState(
    val id: Long,
    val description: String,
    val categoryEnum: CategoryEnum,
)
