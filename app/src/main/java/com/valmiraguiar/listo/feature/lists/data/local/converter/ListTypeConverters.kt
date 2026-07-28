package com.valmiraguiar.listo.feature.lists.data.local.converter

import androidx.room.TypeConverter
import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum

class ListTypeConverters {
    @TypeConverter
    fun fromCategory(value: CategoryEnum): String = value.name

    @TypeConverter
    fun toCategory(value: String): CategoryEnum = CategoryEnum.valueOf(value)

    @TypeConverter
    fun fromUnit(value: UnitEnum): String = value.name

    @TypeConverter
    fun toUnit(value: String): UnitEnum = UnitEnum.valueOf(value)
}
