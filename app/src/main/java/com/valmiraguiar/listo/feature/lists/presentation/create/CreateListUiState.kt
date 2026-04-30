package com.valmiraguiar.listo.feature.lists.presentation.create

data class CreateListUiState(
    val listTitle: String = "",
    val items: List<DraftListItemUiState> = emptyList(),
    val isSaving: Boolean = false,
    val saveConfirmationVisible: Boolean = false,
) {
    val canSubmit: Boolean
        get() = listTitle.isNotBlank() && items.any { it.description.isNotBlank() } && !isSaving
}