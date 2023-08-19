package com.topicality.toDoList.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskName: String,
    var isCompleted: Boolean
)