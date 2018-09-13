package com.uka.noteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.widget.Toast

class DBManager {

    val dbName = "notes"
    val dbTable = "notes"
    val colID = "id"
    val colTitle = "title"
    val colDes = "description"
    val dbVersion = 1

    val sqlCreateTable = "create table if not exists $dbTable($colID int auto_increment primary key, $colTitle text, $colDes text)"

    var sqlDB: SQLiteDatabase? = null

    constructor(context: Context) {
        val db = DatabaseHelper(context)
        sqlDB = db.writableDatabase
    }

    fun insert(value: ContentValues): Long {
        val ID = sqlDB!!.insert(dbTable, "", value)

        return ID
    }

    fun Query(projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = dbTable
        var cursor = queryBuilder.query(sqlDB, projection, selection, selectionArgs, null, null, sortOrder)

        return cursor
    }

    inner class DatabaseHelper : SQLiteOpenHelper {

        var context: Context? = null

        constructor(context: Context) : super(context, dbName, null, dbVersion) {
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)
            Toast.makeText(context, "Database created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("Drop table if exists $dbTable")
        }

    }
}