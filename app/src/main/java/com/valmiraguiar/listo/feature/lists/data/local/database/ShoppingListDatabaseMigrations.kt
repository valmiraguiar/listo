package com.valmiraguiar.listo.feature.lists.data.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object ShoppingListDatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                    ALTER TABLE products
                    ADD COLUMN is_checked INTEGER NOT NULL DEFAULT 0
                """.trimIndent(),
            )
        }
    }
}
