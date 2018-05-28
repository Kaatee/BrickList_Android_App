package com.example.kasia.bricklist

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.content.ContentValues
/**
 * Created by Kasia on 27.05.2018.
 */
class MyDBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "xxx.db"
        val TABLE_BRICKS = "bricks"
        val COLUMN_CODE = "_id"
        val COLUMN_NAME = "name"
        val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_BRICKS_TABLE = ("CREATE TABLE" + TABLE_BRICKS + "("
                + COLUMN_CODE + " VARCHAR PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_IMAGE + " IMAGE"+ ")")
        db?.execSQL(CREATE_BRICKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_BRICKS)
        onCreate(db)
    }
}