package com.example.taskteamapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskteamapp.databinding.ItemTaskBinding

class TaskAdapter(private var tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.taskName.text = task.name
            binding.assignedTo.text = task.assignedTo
            binding.priority.text = task.priority
            binding.statusTextView.text = task.status.toString() // Display task status
            binding.taskDescription.text = task.description // Bind task description
            binding.taskDescription.visibility = if (task.description?.isNotEmpty() == true) {
                View.VISIBLE // Show description if it's not empty
            } else {
                View.GONE // Hide if it's empty
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    // Function to update tasks in the adapter
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged() // Notify adapter of the data changes
    }

    // Optional: Function to add a single task if you need that functionality
    fun addTask(task: Task) {
        tasks = tasks + task // Create a new list with the added task
        notifyItemInserted(tasks.size - 1) // Notify the adapter about the new item
    }

    // Optional: Function to remove a task if you need that functionality
    fun removeTask(task: Task) {
        val updatedTasks = tasks.toMutableList()
        updatedTasks.remove(task) // Remove the task
        tasks = updatedTasks
        notifyDataSetChanged() // Notify the adapter about the data changes
    }
}
