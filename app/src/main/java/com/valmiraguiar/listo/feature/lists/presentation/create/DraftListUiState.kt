package com.valmiraguiar.listo.feature.lists.presentation.create

import com.valmiraguiar.listo.feature.lists.domain.model.ListCategory
import com.valmiraguiar.listo.feature.lists.domain.model.UnitOption

data class DraftListItemUiState(
    val id: Long,
    val quantity: String,
    val unit: UnitOption,
    val description: String,
    val category: ListCategory,
)