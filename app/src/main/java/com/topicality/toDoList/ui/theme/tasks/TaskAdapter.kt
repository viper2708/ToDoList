package com.topicality.toDoList.ui.theme.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.topicality.toDoList.R

class TaskAdapter(private val tasks: List<TaskFragment.Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskRadioButton: RadioButton = itemView.findViewById(R.id.taskRadioButton)
        val taskTextView: TextView = itemView.findViewById(R.id.taskTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskRadioButton.isChecked = task.completed
        holder.taskRadioButton.isEnabled = !task.completed
        holder.taskTextView.text = task.name
        if (task.completed) {
            holder.taskTextView.paintFlags = holder.taskTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.taskTextView.paintFlags = holder.taskTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.taskRadioButton.setOnClickListener {
            task.completed = holder.taskRadioButton.isChecked
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
