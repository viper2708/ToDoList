package com.topicality.toDoList.roomDb

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: TaskEntity)

}
