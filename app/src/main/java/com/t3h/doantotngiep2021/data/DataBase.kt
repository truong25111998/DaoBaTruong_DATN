package com.t3h.doantotngiep2021.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


val DATABASE_NAME = "MyDB"
val TABLE_NAME = "Users"
val COL_NAME = "name"
val COL_AGE = "age"
//val COL_PHONE = "phone"
val COL_ID = "id"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " VARCHAR(256)," +
                COL_AGE + " INTEGER)"
//        +
//                COL_PHONE + " INTEGER)"

        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(user: UserInformation) {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_NAME, user.name)
        cv.put(COL_AGE, user.age)
//        cv.put(COL_PHONE, user.phone)
        var result = db.insert(TABLE_NAME, null, cv)
        if (result == -1.toLong())
            Toast.makeText(context, "Insert Failed", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Insert Success", Toast.LENGTH_SHORT).show()
    }

    fun readData(): MutableList<UserInformation> {
        var list: MutableList<UserInformation> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var user = UserInformation()
                user.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                user.name = result.getString(result.getColumnIndex(COL_NAME))
                user.age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
//                user.phone = result.getString(result.getColumnIndex(COL_PHONE)).toInt()
                list.add(user)
            } while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }


    fun updateData() {
        val db = this.writableDatabase
        val query = "Select * from " + TABLE_NAME
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                var cv = ContentValues()
                cv.put(COL_AGE, (result.getInt(result.getColumnIndex(COL_AGE)) + 1))
                db.update(
                    TABLE_NAME, cv, COL_ID + "=? AND " + COL_NAME + "=?",
                    arrayOf(
                        result.getString(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_NAME))
                    )
                )
            } while (result.moveToNext())
        }

        result.close()
        db.close()
    }


}