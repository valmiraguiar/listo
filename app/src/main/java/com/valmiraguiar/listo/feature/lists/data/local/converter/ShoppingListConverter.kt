package com.valmiraguiar.listo.feature.lists.data.local.converter

import com.valmiraguiar.listo.feature.lists.data.local.entity.ProductEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListEntity
import com.valmiraguiar.listo.feature.lists.data.local.entity.ShoppingListWithProducts
import com.valmiraguiar.listo.feature.lists.domain.model.Product
import com.valmiraguiar.listo.feature.lists.domain.model.ShoppingList

fun ShoppingListWithProducts.toDomain(): ShoppingList {
    return ShoppingList(
        id = this.shoppingList.id,
        title = this.shoppingList.title,
        products = this.products.map { product ->
            product.toDomain()
        },
        createdAt = this.shoppingList.createdAt
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        description = this.description,
        quantity = this.quantity,
        unit = this.unit,
        category = this.category,
        isChecked = this.isChecked,
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        id = this.id,
        title = this.title,
        createdAt = this.createdAt
    )
}

fun Product.toEntity(shoppingListId: Long): ProductEntity {
    return ProductEntity(
        id = this.id,
        shoppingListId = shoppingListId,
        description = this.description,
        category = this.category,
        unit = this.unit,
        quantity = this.quantity,
        isChecked = this.isChecked,
    )
}
