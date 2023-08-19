import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.topicality.toDoList.R
import com.topicality.toDoList.data.TaskDatabaseHelper
import com.topicality.toDoList.ui.theme.tasks.TaskAdapter

class TaskFragment : Fragment() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var databaseHelper: TaskDatabaseHelper
    private val tasks = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseHelper = TaskDatabaseHelper(requireContext())

        val taskRecyclerView = view.findViewById<RecyclerView>(R.id.taskRecyclerView)
        taskAdapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = taskAdapter

        // Set a layout manager for the RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val addTaskButton = view.findViewById<Button>(R.id.addTaskButton)
        addTaskButton.setOnClickListener {
            Toast.makeText(requireContext(), "Button Clicked", Toast.LENGTH_SHORT).show()
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
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        addTaskButtonPopup.setOnClickListener {
            val taskName = taskNameEditText.text.toString()

            if (taskName.isNotEmpty()) {
                tasks.add(taskName)
                taskAdapter.notifyDataSetChanged()

                if (::databaseHelper.isInitialized) {
                    databaseHelper.insertTask(taskName)
                }
            }

            // Dismiss the popup window
            popupWindow.dismiss()
        }

        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0)
    }

}
