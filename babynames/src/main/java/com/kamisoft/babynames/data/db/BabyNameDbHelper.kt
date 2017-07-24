package com.kamisoft.babynames.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.kamisoft.babynames.BabyNamesApp
import org.jetbrains.anko.db.*

class BabyNameDbHelper(ctx: Context = BabyNamesApp.instance) : ManagedSQLiteOpenHelper(ctx,
        BabyNameDbHelper.DB_NAME, null, BabyNameDbHelper.DB_VERSION) {

    companion object {
        val DB_NAME = "babyname.db"
        val DB_VERSION = 1
        val instance by lazy { BabyNameDbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(FavoriteTable.NAME, true,
                FavoriteTable.ID to INTEGER + PRIMARY_KEY,
                FavoriteTable.PARENT to TEXT,
                FavoriteTable.GENDER to INTEGER,
                FavoriteTable.BABY_NAME to TEXT)
    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        //TODO Not implemented yet.
    }

}