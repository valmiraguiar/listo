package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ShoppingListWithProductsEntity(
    @Embedded val list: ShoppingListEntity,
    @Relation(
        entity = ProductEntity::class,
        parentColumn = "list_id",
        entityColumn = "product_id",
        associateBy = Junction(
            value = ShoppingListProductEntity::class,
            parentColumn = "list_id",
            entityColumn = "product_id"
        )
    )
    val products: List<ProductEntity>
)
