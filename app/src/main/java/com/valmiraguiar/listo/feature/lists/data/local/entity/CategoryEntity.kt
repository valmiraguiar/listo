package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo("category_id")
    val id: Long = 0L,
    @ColumnInfo("category_name")
    val categoryName: String,
)
