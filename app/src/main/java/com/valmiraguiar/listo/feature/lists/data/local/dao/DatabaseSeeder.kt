package com.valmiraguiar.listo.feature.lists.data.local.dao

import com.valmiraguiar.listo.core.database.CategorySeedData
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val categoryDao: CategoryDao,
) {
    suspend fun seedCategories() {
        categoryDao.insertAll(CategorySeedData.entities())
    }
}
