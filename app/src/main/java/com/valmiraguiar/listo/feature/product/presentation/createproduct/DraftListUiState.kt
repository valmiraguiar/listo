package com.valmiraguiar.listo.feature.product.presentation.createproduct

import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum

data class DraftListItemUiState(
    val id: Long,
    val quantity: String,
    val unit: UnitEnum,
    val description: String,
    val categoryEnum: CategoryEnum,
)