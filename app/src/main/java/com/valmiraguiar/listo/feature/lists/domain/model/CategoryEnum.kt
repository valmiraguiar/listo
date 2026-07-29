package com.valmiraguiar.listo.feature.lists.domain.model

enum class CategoryEnum(val id: Long) {
    Beverages(id = 1),
    Grocery(id = 2),
    Dairy(id = 3),
    Meat(id = 4);

    companion object {
        fun fromId(id: Long) = entries.first { it.id == id }
    }
}
