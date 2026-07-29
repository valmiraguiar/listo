package com.valmiraguiar.listo.feature.lists.domain.model

data class ShoppingListDetails(
    val id: Long,
    val title: String,
    val products: List<ShoppingListProduct>,
)

data class ShoppingListProduct(
    val id: Long,
    val title: String,
    val quantity: String,
    val unit: UnitEnum,
    val categoryName: String,
)
