package com.valmiraguiar.listo.feature.lists.data.local.converter

import androidx.room.TypeConverter
import com.valmiraguiar.listo.feature.lists.domain.model.ListCategory
import com.valmiraguiar.listo.feature.lists.domain.model.UnitOption

class ListTypeConverters {
    @TypeConverter
    fun fromCategory(value: ListCategory): String = value.name

    @TypeConverter
    fun toCategory(value: String): ListCategory = ListCategory.valueOf(value)

    @TypeConverter
    fun fromUnit(value: UnitOption): String = value.name

    @TypeConverter
    fun toUnit(value: String): UnitOption = UnitOption.valueOf(value)
}
