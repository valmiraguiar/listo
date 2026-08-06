package com.valmiraguiar.listo.feature.lists.data.local.converter

import androidx.room.TypeConverter
import com.valmiraguiar.listo.feature.lists.domain.model.CategoryEnum

class CategoryConverter {
    @TypeConverter
    fun fromCategory(category: CategoryEnum): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): CategoryEnum {
        return CategoryEnum.valueOf(value)
    }
}