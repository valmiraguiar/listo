package com.valmiraguiar.listo.feature.product.presentation

data class CreateProductUiState(
    val items: List<DraftListItemUiState> = emptyList(),
    val isSaving: Boolean = false,
) {
    val canSubmit: Boolean
        get() = items.any { it.description.isNotBlank() } && !isSaving
}