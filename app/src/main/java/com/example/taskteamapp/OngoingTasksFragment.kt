package com.example.taskteamapp

import TaskRepository
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class OngoingTasksFragment : Fragment() {

    private lateinit var teamId: String
    private lateinit var firestore: FirebaseFirestore
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamId = arguments?.getString(ARG_TEAM_ID) ?: throw IllegalArgumentException("Team ID is required")
        firestore = FirebaseFirestore.getInstance() // Initialize Firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ongoing_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.ongoingTasksRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        taskAdapter = TaskAdapter(emptyList())
        recyclerView.adapter = taskAdapter

        // Fetch ongoing tasks for the specific team
        fetchOngoingTasks()
    }

    private fun fetchOngoingTasks() {
        firestore.collection("teams").document(teamId).collection("tasks")
            .whereEqualTo("status", TaskStatus.ONGOING.name) // Fetch tasks with ongoing status
            .get()
            .addOnSuccessListener { documents ->
                val ongoingTasks = documents.toObjects(Task::class.java) // Convert Firestore documents to Task objects
                taskAdapter.updateTasks(ongoingTasks) // Update the adapter with the new tasks
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to load ongoing tasks", Toast.LENGTH_SHORT).show()
            }
    }

    inner class TaskAdapter(private var tasks: List<Task>) :
        RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

        inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val taskName: TextView = itemView.findViewById(R.id.taskName)
            private val assignedTo: TextView = itemView.findViewById(R.id.assignedTo)
            private val priority: TextView = itemView.findViewById(R.id.priority)
            private val updateStatusButton: Button = itemView.findViewById(R.id.updateStatusButton)

            fun bind(task: Task) {
                taskName.text = task.name
                assignedTo.text = task.assignedTo
                priority.text = task.priority

                // Set the update status button logic
                updateStatusButton.setOnClickListener {
                    updateTaskStatus(task.id) // Call the function to update the task status
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
            return TaskViewHolder(view)
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
    }

    private fun updateTaskStatus(taskId: String) {
        // Assuming you have a TaskRepository with an updateTaskStatus method
        TaskRepository().updateTaskStatus(taskId, TaskStatus.COMPLETED) // Update status to COMPLETED
        fetchOngoingTasks() // Refresh the ongoing tasks
        Toast.makeText(context, "Task marked as completed", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARG_TEAM_ID = "teamId"

        fun newInstance(teamId: String): OngoingTasksFragment {
            val fragment = OngoingTasksFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_ID, teamId)
            fragment.arguments = args
            return fragment
        }
    }
}
