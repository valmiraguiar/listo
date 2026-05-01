package com.valmiraguiar.listo.feature.lists.domain.model

import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum

data class ShoppingListDraft(
    val title: String,
    val items: List<ShoppingListDraftItem>,
)

data class ShoppingListDraftItem(
    val quantity: String,
    val unit: UnitEnum,
    val description: String,
    val categoryEnum: CategoryEnum,
)
