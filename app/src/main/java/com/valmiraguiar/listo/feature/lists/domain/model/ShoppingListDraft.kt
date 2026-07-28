package com.valmiraguiar.listo.feature.lists.domain.model


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
