package com.valmiraguiar.listo.feature.lists.data.local.converter

import androidx.room.TypeConverter
import com.valmiraguiar.listo.feature.lists.domain.model.UnitEnum

class UnitConverter {
    @TypeConverter
    fun fromUnit(value: UnitEnum): String = value.name

    @TypeConverter
    fun toUnit(value: String): UnitEnum = UnitEnum.valueOf(value)
}
