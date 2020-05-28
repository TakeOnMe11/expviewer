package com.example.expviewer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun deleteDB(){
        context.deleteDatabase(DATABASE_NAME)
    }

    companion object {
        val DATABASE_NAME = "dbExpViewer.db"
        private val DATABASE_VERSION = 1
    }
}