package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.ColumnInfo
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum

data class ProductWithCategoryEntity(
    @ColumnInfo(name = "product_id")
    val productId: Long,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "quantity")
    val quantity: String,
    @ColumnInfo(name = "unit")
    val unit: UnitEnum,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
)
