package com.topicality.toDoList

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.topicality.toDoList.data.TaskDatabaseHelper
import com.topicality.toDoList.ui.theme.ToDoListTheme
import com.topicality.toDoList.ui.theme.tasks.TaskFragment

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: TaskDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper = TaskDatabaseHelper(this)

        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer, TaskFragment())
            fragmentTransaction.commit()
        }
    }

    // ... other methods ...
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ToDoListTheme {
        Greeting("Android")
    }
}