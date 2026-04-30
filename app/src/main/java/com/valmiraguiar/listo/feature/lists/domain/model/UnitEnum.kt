package com.valmiraguiar.listo.feature.lists.domain.model

enum class UnitEnum(val id: Long) {
    Unit(id = 1),
    Kilogram(id = 2),
    Gram(id = 3),
    Liter(id = 4),
    Milliliter(id = 5),
    Pack(id = 6);

    companion object {
        fun fromId(id: Long) = UnitEnum.entries.first { it.id == id }
    }
}
