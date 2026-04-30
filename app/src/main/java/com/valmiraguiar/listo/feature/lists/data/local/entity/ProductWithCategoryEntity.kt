package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.ColumnInfo

data class ProductWithCategoryEntity(
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "category_name")
    val categoryName: String
)
