package com.valmiraguiar.listo.feature.lists.domain.model

data class ShoppingList(
    val id: Long,
    val title: String,
    val products: List<Product>,
    val createdAt: Long
)
