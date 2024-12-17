package com.example.taskteamapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskteamapp.databinding.ItemTeamBinding

class TeamAdapter(
    private val teams: List<Team>,
    private val onJoinClick: (Team) -> Unit,
    private val onAssignTaskClick: (Team) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bind(team)
    }

    override fun getItemCount(): Int = teams.size

    inner class TeamViewHolder(private val binding: ItemTeamBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
            binding.teamNameTextView.text = team.name
            binding.joinTeamButton.setOnClickListener { onJoinClick(team) }
            binding.assignTaskButton.setOnClickListener { onAssignTaskClick(team) }
        }
    }
}
