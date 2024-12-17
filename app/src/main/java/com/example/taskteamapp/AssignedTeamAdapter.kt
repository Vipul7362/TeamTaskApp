package com.example.taskteamapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskteamapp.databinding.ItemAssignedTeamBinding

class AssignedTeamAdapter(
    private val teams: List<Team>,
    private val onAssignTaskClick: (Team) -> Unit
) : RecyclerView.Adapter<AssignedTeamAdapter.AssignedTeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignedTeamViewHolder {
        val binding = ItemAssignedTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssignedTeamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssignedTeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }

    override fun getItemCount(): Int = teams.size

    inner class AssignedTeamViewHolder(private val binding: ItemAssignedTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
            binding.teamNameTextView.text = team.name
            binding.assignTaskButton.setOnClickListener { onAssignTaskClick(team) }
        }
    }
}
