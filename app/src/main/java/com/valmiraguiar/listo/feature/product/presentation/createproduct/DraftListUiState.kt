package com.valmiraguiar.listo.feature.product.presentation.createproduct

import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum

data class DraftListItemUiState(
    val id: Long,
    val quantity: String,
    val unit: UnitEnum,
    val description: String,
    val categoryEnum: CategoryEnum,
)
