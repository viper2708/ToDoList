package com.topicality.toDoList.ui.theme.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupWindow
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import com.topicality.toDoList.R
import com.topicality.toDoList.data.TaskDatabaseHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskFragment : Fragment() {

    private lateinit var taskEditText: EditText
    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: TaskListAdapter
    private val tasks = ArrayList<String>()
    private lateinit var databaseHelper: TaskDatabaseHelper
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize databaseHelper before using it
        databaseHelper = TaskDatabaseHelper(requireContext())

        rootView = inflater.inflate(R.layout.fragment_task, container, false)
        taskListView = rootView.findViewById(R.id.taskListView)
        taskAdapter = TaskListAdapter(requireContext(), android.R.layout.simple_list_item_1, tasks)
        taskListView.adapter = taskAdapter

        val addTaskButton = rootView.findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            addTask()
        }

        // Retrieve tasks from the database and populate the tasks list
        tasks.addAll(databaseHelper.getAllTasks())
        taskAdapter.notifyDataSetChanged()

        // Initialize other UI components here
        return rootView
    }

    fun addTask() {
        val popupView = layoutInflater.inflate(R.layout.popup_add_task, null)

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0)

        val addTaskButton = popupView.findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            addTaskFromPopup(popupView)
            popupWindow.dismiss()
        }
    }

    fun addTaskFromPopup(popupView: View) {
        val taskNameEditText = popupView.findViewById<EditText>(R.id.taskNameEditText)
        val addTaskButton = popupView.findViewById<Button>(R.id.addTaskButton)

        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0)

        addTaskButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)

                    val formattedDate = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()
                    ).format(calendar.time)
                    val taskName = taskNameEditText.text.toString()

                    if (taskName.isNotEmpty()) {
                        tasks.add(taskName)
                        taskAdapter.notifyDataSetChanged()

                        // TODO: Save the task with due date and task name
                        if (::databaseHelper.isInitialized) {
                            databaseHelper.insertTaskWithDueDate(taskName, formattedDate)
                        }
                    }

                    popupWindow.dismiss()
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            datePicker.show()
        }
    }

}
