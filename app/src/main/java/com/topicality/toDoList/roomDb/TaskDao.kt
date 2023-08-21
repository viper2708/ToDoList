package com.topicality.toDoList.roomDb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

}
