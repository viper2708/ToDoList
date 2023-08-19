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
        const val COLUMN_TASK = "task"
        const val COLUMN_DUE_DATE = "dueDate"
        private const val TABLE_CREATE =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TASK TEXT NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTaskWithDueDate(task: String, dueDate: String) {
        val values = ContentValues()
        values.put(COLUMN_TASK, task)
        values.put(COLUMN_DUE_DATE, dueDate)

        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }


    fun getAllTasks(): List<String> {
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
    }
    // ... other database-related methods ...
}
