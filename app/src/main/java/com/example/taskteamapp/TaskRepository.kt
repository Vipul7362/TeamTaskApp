import com.example.taskteamapp.Task
import com.example.taskteamapp.TaskStatus

// TaskRepository.kt
class TaskRepository {
    private val tasks = mutableListOf<Task>()

    // Add a new task
    fun addTask(task: Task) {
        tasks.add(task)
    }

    // Get tasks by status
    fun getTasksByStatus(status: TaskStatus): List<Task> {
        return tasks.filter { it.status == status }
    }

    // Get tasks by status and team ID
    fun getTasksByStatusAndTeam(status: TaskStatus, teamId: String): List<Task> {
        return tasks.filter { it.status == status && it.teamId == teamId } // Assuming Task has a teamId property
    }

    // Update task status
    fun updateTaskStatus(taskId: String, newStatus: TaskStatus) {
        val task = tasks.find { it.id == taskId }
        task?.let {
            val index = tasks.indexOf(it)
            tasks[index] = it.copy(status = newStatus) // Update the status
        }
    }

    // Additional method to get all tasks (if needed)
    fun getAllTasks(): List<Task> {
        return tasks
    }
}
