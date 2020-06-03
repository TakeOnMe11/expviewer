package com.example.expviewer

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class ExpDB(val context: Context) {
    private val ID = "id"
    private val NAME = "name"
    private val MEASDATE = "measdate"
    private val COMMENT = "comment"
    private val DATA = "data"
    private val SETID_NAME = "setid_name"
    private val SETID_DESC = "setid_desc"
    private val TYPEID_DESC = "typeid_desc"
    private val TABLE_NAME = "expdata"
    private var database: Database? = null
    private var sqLiteDatabase: SQLiteDatabase? = null

    init {
        database = Database(context)
        sqLiteDatabase = database!!.writableDatabase
        createTable()
    }

    private fun createTable() {
        val DATABASE_CREATE_SCRIPT = " create table if not exists " +
                TABLE_NAME + " (" + ID + " integer primary key, " +
                NAME + " text not null, " +
                MEASDATE + " text not null, " +
                COMMENT + " text not null, " +
                DATA + " text not null, " +
                SETID_NAME + " text not null, " +
                SETID_DESC + " text not null, " +
                TYPEID_DESC + " text not null);"
        sqLiteDatabase!!.execSQL(DATABASE_CREATE_SCRIPT)
    }

    fun closeDB(){
        if(sqLiteDatabase!!.isOpen){
            sqLiteDatabase!!.close()
        }
    }

    fun clearTableDB() {
        sqLiteDatabase!!.execSQL(" delete from " + TABLE_NAME)
    }

    fun addAllExp(arrayExp: List<ExpData>) {
        for (exp in arrayExp) {
            addExp(exp)
        }
    }

    fun addExp(exp: ExpData) {
        if (exp.name.isEmpty()) {
            exp.name = context.getString(R.string.no_name)
        }
        if (exp.measDate.isEmpty()) {
            exp.measDate = context.getString(R.string.no_date)
        }
        if (exp.comment.isEmpty()) {
            exp.comment = context.getString(R.string.no_comment)
        }
        if (exp.setName.isEmpty()) {
            exp.setName = context.getString(R.string.no_set_name)
        }
//        if (exp.setDescription.isEmpty()) {
//            exp.setDescription = context.getString(R.string.no_set_desc)
//        }
//        if (exp.typeName.isEmpty()) {
//            exp.typeName = context.getString(R.string.no_type)
//        }

        val query = " INSERT OR REPLACE INTO " + TABLE_NAME + " VALUES(" + exp.id + ",\"" +
                exp.name + "\",\"" + exp.measDate + "\",\"" + exp.comment + "\",\"" +
                exp.data + "\",\"" + exp.setName + "\",\"" + exp.setDescription + "\",\"" +
                exp.typeName + "\");"
        sqLiteDatabase!!.execSQL(query)
    }

    fun getSingleExp(id: Int): ExpData {
        val query = " SELECT * FROM " + TABLE_NAME + " WHERE " + ID + "=" + id
        val cursor = sqLiteDatabase!!.rawQuery(query, null)
        var exp = ExpData()
        if (cursor.count != 0) {
            cursor.moveToFirst()
            exp = getExp(cursor)
        }
        cursor.close()
        return exp
    }

    fun getArrayExp(): ArrayList<ExpData> {
        val query = " SELECT * FROM " + TABLE_NAME
        val cursor = sqLiteDatabase!!.rawQuery(query, null)
        val arrayExp = arrayListOf<ExpData>()
        while (cursor.moveToNext()) {
            arrayExp.add(getExp(cursor))
        }
        cursor.close()

        return arrayExp
    }

    private fun getExp(cursor: Cursor): ExpData {
        val exp = ExpData()
        exp.id = cursor.getInt(cursor.getColumnIndex(ID))
        exp.name = cursor.getString(cursor.getColumnIndex(NAME))
        exp.measDate = cursor.getString(cursor.getColumnIndex(MEASDATE))
        exp.comment = cursor.getString(cursor.getColumnIndex(COMMENT))
        exp.data = cursor.getString(cursor.getColumnIndex(DATA))
        exp.setName = cursor.getString(cursor.getColumnIndex(SETID_NAME))
        exp.setDescription = cursor.getString(cursor.getColumnIndex(SETID_DESC))
        exp.typeName = cursor.getString(cursor.getColumnIndex(TYPEID_DESC))

        return exp
    }
}