package com.valmiraguiar.listo.core.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object ListoDatabaseMigrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {

        override fun migrate(db: SupportSQLiteDatabase) {

            createCategoryTable(db)
            createProductsTable(db)
            createShoppingListProductTable(db)

            CategorySeedData.sync(db)
        }
    }

    private fun createCategoryTable(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS category (
                category_id INTEGER NOT NULL,
                category_name TEXT NOT NULL,
                PRIMARY KEY(category_id)
            )
            """
        )
    }

    private fun createProductsTable(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS products (
                product_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                product_name TEXT NOT NULL,
                quantity TEXT NOT NULL,
                unit TEXT NOT NULL,
                category_id INTEGER NOT NULL,
                FOREIGN KEY(category_id)
                    REFERENCES category(category_id)
                    ON DELETE CASCADE
            )
            """
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_products_category_id
            ON products(category_id)
            """
        )
    }

    private fun createShoppingListProductTable(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS shopping_list_product (
                list_id INTEGER NOT NULL,
                product_id INTEGER NOT NULL,
                PRIMARY KEY(list_id, product_id),
                FOREIGN KEY(list_id)
                    REFERENCES shopping_lists(list_id)
                    ON DELETE CASCADE,
                FOREIGN KEY(product_id)
                    REFERENCES products(product_id)
                    ON DELETE CASCADE
            )
            """
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_shopping_list_product_list_id
            ON shopping_list_product(list_id)
            """
        )

        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS index_shopping_list_product_product_id
            ON shopping_list_product(product_id)
            """
        )
    }
}
