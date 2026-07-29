package com.valmiraguiar.listo.feature.lists.presentation.details.state

import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum

data class ShoppingListDetailsUiState(
    val isLoading: Boolean = true,
    val listId: Long = 0L,
    val title: String = "",
    val items: List<ShoppingListDetailsItemUiState> = emptyList(),
    val isNotFound: Boolean = false,
)

data class ShoppingListDetailsItemUiState(
    val id: Long,
    val title: String,
    val classification: String,
    val quantity: String = "",
    val unit: UnitEnum = UnitEnum.Unit,
    val isChecked: Boolean = false,
)
