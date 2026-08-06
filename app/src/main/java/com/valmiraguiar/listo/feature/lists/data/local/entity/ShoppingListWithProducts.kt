package com.valmiraguiar.listo.feature.lists.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ShoppingListWithProducts(
    @Embedded
    val shoppingList: ShoppingListEntity,
    @Relation(
        parentColumn = "list_id",
        entityColumn = "shopping_list_id",
        entity = ProductEntity::class
    )
    val products: List<ProductEntity>
)