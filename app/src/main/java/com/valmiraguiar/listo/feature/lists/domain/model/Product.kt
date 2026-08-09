package com.valmiraguiar.listo.feature.lists.domain.model

data class Product(
    val id: Long,
    val description: String,
    val quantity: String,
    val unit: UnitEnum,
    val category: CategoryEnum,
    val isChecked: Boolean = false,
)
