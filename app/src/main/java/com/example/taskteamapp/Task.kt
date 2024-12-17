package com.example.taskteamapp

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(), // Auto-generated unique ID
    val name: String = "", // Default value for name
    val teamId: String = "", // Default value for teamId
    val description: String? = null, // Optional task description
    val assignedTo: String = "", // Default value for assignedTo
    val status: TaskStatus = TaskStatus.ONGOING, // Default status
    val priority: String = "Medium", // Default priority
    val dueDate: Long? = null, // Due date (optional), stored as Unix timestamp
    val createdAt: Long = System.currentTimeMillis(), // Timestamp of task creation
    val updatedAt: Long = System.currentTimeMillis(), // Timestamp of last update
    val attachments: List<String>? = null // Optional list of attachment URLs or file paths
) {
    // No-argument constructor for Firestore deserialization
    constructor() : this(
        id = UUID.randomUUID().toString(),
        name = "",
        teamId = "",
        assignedTo = "",
        status = TaskStatus.ONGOING,
        priority = "Medium",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}

enum class TaskStatus {
    ONGOING, COMPLETED, UPCOMING
}
