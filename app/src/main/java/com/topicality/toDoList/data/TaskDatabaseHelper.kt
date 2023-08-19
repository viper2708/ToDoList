package com.topicality.toDoList.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "task_database"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tasks"
        const val COLUMN_ID = "_id"
        const val COLUMN_TASK_NAME = "task"
        private const val TABLE_CREATE =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TASK_NAME TEXT NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(taskName: String): Long {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK_NAME, taskName)

        val db = writableDatabase
        return db.insert(TABLE_NAME, null, contentValues)
    }


    /*fun getAllTasks(): List<String> {
        val tasks = ArrayList<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_TASK FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val task = cursor.getString(cursor.getColumnIndex(COLUMN_TASK))
                tasks.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }*/
    // ... other database-related methods ...
}
