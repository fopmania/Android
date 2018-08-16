package com.mugdog.nicetimer.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


//val COL_STRING = "time_string"


class DBHelper(var context: Context ) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        internal const val DATABASE_NAME = "nicetimer.db"
        internal const val DATABASE_VERSION = 1
        internal const val TABLE_NAME = "tb_timer"
        internal const val COL_ID = "id"
        internal const val COL_NAME = "name"
        internal const val COL_SECONDS = "time_seconds"
        internal const val COL_DATE = "date"
    }




    private val CREATE_TABLE = ("create table if not exists " + TABLE_NAME
            + " (" + COL_ID + " integer primary key autoincrement, " + COL_NAME
            + " text not null, "// + TIME_STRING + " text not null, "
//            + COL_SECONDS + " real not null);")
            + COL_SECONDS + " real not null, " + COL_DATE + " datetime default current_timestamp ); ")

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

//        if(oldVersion != newVersion) {
//            when(newVersion){
//                2 -> {
//                    db?.execSQL("ALTER TABLE $TABLE_NAME  ADD COLUMN $COL_DATE DATE")
//                }
//            }
//        }
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    fun insertData(nt : NTimer){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_NAME, nt.name)
        cv.put(COL_SECONDS, nt.seconds)

        val result = db.insert(TABLE_NAME, null, cv)
        if(result == (-1).toLong())
            Toast.makeText(context, "add Failed", Toast.LENGTH_SHORT ).show()
        else
            Toast.makeText(context, "New timer added ", Toast.LENGTH_SHORT ).show()

    }

    fun readData() : MutableList<NTimer> {
        var list: MutableList<NTimer> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME + " order by $COL_DATE desc"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var nt = NTimer(
                        result.getString(result.getColumnIndex(COL_ID)).toInt(),
                                result.getString(result.getColumnIndex(COL_NAME)),
                                        result.getString(result.getColumnIndex(COL_SECONDS)).toFloat() )
                list.add(nt)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun deleteData(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, COL_ID+"=?", arrayOf(id.toString()))
        db.close()
    }
}

