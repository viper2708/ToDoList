package com.topicality.toDoList.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TaskDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "task_database"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TASK_NAME = "task_name"
        private const val COLUMN_COMPLETED = "completed" // New column
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TASK_NAME TEXT, $COLUMN_COMPLETED INTEGER)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add any necessary schema changes for version 2
            db.execSQL("ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_COMPLETED INTEGER DEFAULT 0")
        }
    }

    fun insertTask(taskName: String, completed: Boolean): Long {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TASK_NAME, taskName)
        contentValues.put(COLUMN_COMPLETED, if (completed) 1 else 0)

        val db = writableDatabase
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllTasks(): List<TaskFragment.Task> {
        val tasks = mutableListOf<TaskFragment.Task>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        while (cursor.moveToNext()) {
            val taskName = cursor.getString(cursor.getColumnIndex(COLUMN_TASK_NAME))
            val completed = cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1
            tasks.add(TaskFragment.Task(taskName, completed))
        }

        cursor.close()
        return tasks
    }

    // ... rest of the code
}

