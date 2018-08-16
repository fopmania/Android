package com.mugdog.nicetimer.service

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import com.mugdog.nicetimer.model.DBHelper

class MyContentProvider : ContentProvider() {

    lateinit var dbHelper : DBHelper
    lateinit var db : SQLiteDatabase


    override fun onCreate(): Boolean {
        dbHelper = DBHelper(context)
        db = dbHelper.writableDatabase

        return true

    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement this to handle requests for the MIME type of the data" +
                "at the given URI")
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }


    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        TODO("Implement this to handle query requests from clients.")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
