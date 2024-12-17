package com.example.taskteamapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskteamapp.databinding.ActivityTaskAssignmentBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class TaskAssignmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskAssignmentBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var teamId: String
    private var dueDate: Long? = null // Store selected due date as Unix timestamp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        teamId = intent.getStringExtra("teamId")!!

        firestore = FirebaseFirestore.getInstance()

        // Setup priority spinner
        val priorities = listOf("High", "Medium", "Low")
        val priorityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        binding.prioritySpinner.adapter = priorityAdapter

        binding.dueDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        binding.assignTaskButton.setOnClickListener {
            val taskName = binding.taskNameEditText.text.toString().trim()
            val assignedTo = binding.assignedToEditText.text.toString().trim()
            val priority = binding.prioritySpinner.selectedItem.toString()
            val description = binding.descriptionEditText.text.toString().trim()

            if (taskName.isEmpty() || assignedTo.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            assignTask(taskName, assignedTo, priority, dueDate, description)
        }

        binding.viewTaskButton.setOnClickListener {
            val intent = Intent(this, TaskOverviewActivity::class.java)
            intent.putExtra("teamId", teamId)
            startActivity(intent)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Set the minimum date to today to prevent selecting past dates
        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(selectedYear, selectedMonth, selectedDay)
            dueDate = selectedDate.timeInMillis // Convert to Unix timestamp
            binding.dueDateEditText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear") // Format date for display
        }, year, month, day)

        // Set the minimum date to today
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // Prevent past date selection
        datePickerDialog.show()
    }

    private fun assignTask(taskName: String, assignedTo: String, priority: String, dueDate: Long?, description: String?) {
        val taskStatus = TaskStatus.ONGOING // Set the default task status as ONGOING

        // Create the Task object
        val task = Task(
            teamId = teamId,  // Corrected assignment of teamId
            name = taskName,
            description = description,
            assignedTo = assignedTo,
            status = taskStatus,
            priority = priority,
            dueDate = dueDate
        )

        // Add the task to Firestore under the team’s collection
        firestore.collection("teams").document(teamId).collection("tasks").add(task)
            .addOnSuccessListener {
                Toast.makeText(this, "Task assigned successfully", Toast.LENGTH_SHORT).show()
                // Clear the input fields after success
                binding.taskNameEditText.text?.clear()
                binding.assignedToEditText.text?.clear()
                binding.dueDateEditText.text?.clear()
                binding.descriptionEditText.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to assign task", Toast.LENGTH_SHORT).show()
            }
    }
}
