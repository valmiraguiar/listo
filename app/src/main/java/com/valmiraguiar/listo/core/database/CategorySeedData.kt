package com.valmiraguiar.listo.core.database

import androidx.sqlite.db.SupportSQLiteDatabase
import com.valmiraguiar.listo.feature.lists.data.local.entity.CategoryEntity
import com.valmiraguiar.listo.feature.product.domain.model.CategoryEnum

object CategorySeedData {
    fun entities(): List<CategoryEntity> {
        return CategoryEnum.entries.map { category ->
            CategoryEntity(
                id = category.id,
                categoryName = category.name,
            )
        }
    }

    fun sync(database: SupportSQLiteDatabase) {
        entities().forEach { category ->
            database.execSQL(
                "INSERT OR REPLACE INTO category (category_id, category_name) VALUES (?, ?)",
                arrayOf<Any>(category.id, category.categoryName),
            )
        }
    }
}
