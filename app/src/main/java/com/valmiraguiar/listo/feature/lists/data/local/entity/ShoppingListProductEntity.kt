package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "shopping_list_product",
    primaryKeys = ["list_id", "product_id"],
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["list_id"],
            childColumns = ["list_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("list_id"),
        Index("product_id"),
    ]
)
data class ShoppingListProductEntity(
    @ColumnInfo(name = "list_id")
    val listId: Long,
    @ColumnInfo(name = "product_id")
    val productId: Long
)
