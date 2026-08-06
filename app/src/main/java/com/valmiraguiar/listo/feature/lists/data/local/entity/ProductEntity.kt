package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum

@Entity(
    tableName = "products",
    indices = [Index("category_id")]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val id: Long = 0L,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "quantity")
    val quantity: String,
    @ColumnInfo(name = "unit")
    val unit: UnitEnum,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
)
