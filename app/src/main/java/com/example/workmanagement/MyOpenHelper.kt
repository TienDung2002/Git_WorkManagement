package com.example.workmanagement

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {
    companion object {
        private val DATABASE_NAME = "WorkManagement.db"
        private val DB_VERSION = 1
        const val TABLE_NAME = "work"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "work name"
        const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_TIME TEXT);"
        db?.execSQL(createTable)
    }

    // nếu bảng trùng tên đã được tạo thì xóa rồi tạo mới
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}