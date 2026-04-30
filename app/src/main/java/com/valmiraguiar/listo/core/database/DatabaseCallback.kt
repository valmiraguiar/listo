package com.valmiraguiar.listo.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.Inject

class DatabaseCallback @Inject constructor() : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        CategorySeedData.sync(db)
    }
}
