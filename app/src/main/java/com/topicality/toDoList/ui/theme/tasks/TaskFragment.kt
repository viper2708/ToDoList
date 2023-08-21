import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.topicality.toDoList.R
import com.topicality.toDoList.roomDb.AppDatabase
import com.topicality.toDoList.roomDb.TaskDao
import com.topicality.toDoList.roomDb.TaskEntity
import com.topicality.toDoList.ui.theme.tasks.TaskAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TaskFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private val tasks = mutableListOf<TaskEntity>()
    private lateinit var taskDao: TaskDao
    private lateinit var database: AppDatabase
    private val fragmentScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Room database
        database = Room.inMemoryDatabaseBuilder(requireContext(), AppDatabase::class.java).build()
        taskDao = database.taskDao()
        loadTasks()

        val taskRecyclerView = view.findViewById<RecyclerView>(R.id.taskRecyclerView)
        taskAdapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = taskAdapter

        // Set a layout manager for the RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val addTaskButton = view.findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            val popupView = layoutInflater.inflate(R.layout.popup_add_task, null)
            addTaskFromPopup(popupView)
        }

    }

    private fun addTaskFromPopup(rootView: View) {
        val taskNameEditText = rootView.findViewById<EditText>(R.id.taskNameEditText)
        val addTaskButtonPopup = rootView.findViewById<Button>(R.id.addTaskButtonPopup)

        // Show the popup window
        val popupWindow = PopupWindow(
            rootView,
            (Resources.getSystem().displayMetrics.widthPixels * 0.8).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        addTaskButtonPopup.setOnClickListener {
            val taskName = taskNameEditText.text.toString()

            if (taskName.isNotEmpty()) {
                val task = TaskEntity(taskName = taskName, isCompleted = false)
                insertTask(task)
                tasks.add(task)
                taskAdapter.notifyDataSetChanged()
            }

            // Dismiss the popup window
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0)
    }

    fun insertTask(task: TaskEntity) {
        fragmentScope.launch {
            database.taskDao().insertTask(task)
        }
    }

    // TaskFragment
    private fun loadTasks() {
        fragmentScope.launch {
            val tasks = taskDao.getAllTasks()
            taskAdapter.updateTasks(tasks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentScope.cancel()
    }


}
