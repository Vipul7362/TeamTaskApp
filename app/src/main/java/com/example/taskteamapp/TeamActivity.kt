package com.example.taskteamapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskteamapp.databinding.ActivityTeamBinding
import com.google.firebase.firestore.FirebaseFirestore

class TeamActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamBinding
    private lateinit var firestore: FirebaseFirestore
    private val teamList = mutableListOf<Team>()
    private val adapter = TeamAdapter(teamList, this::joinTeam, this::openTaskAssignment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        binding.teamRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.teamRecyclerView.adapter = adapter

        binding.createTeamButton.setOnClickListener {
            val teamName = binding.teamNameEditText.text.toString().trim()
            if (teamName.isNotEmpty()) {
                createTeam(teamName)
            } else {
                Toast.makeText(this, "Team name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        loadTeams()
    }

    private fun createTeam(name: String) {
        val teamId = firestore.collection("teams").document().id
        val team = Team(teamId, name, listOf("user@example.com"))

        firestore.collection("teams").document(teamId).set(team)
            .addOnSuccessListener {
                Toast.makeText(this, "Team created successfully", Toast.LENGTH_SHORT).show()
                binding.teamNameEditText.text?.clear()
                loadTeams()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create team", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadTeams() {
        firestore.collection("teams").get()
            .addOnSuccessListener { documents ->
                teamList.clear()
                for (document in documents) {
                    val team = document.toObject(Team::class.java)
                    teamList.add(team)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun joinTeam(team: Team) {
        val currentUserEmail = "user@example.com"  // Replace with actual logged-in user email
        if (!team.members.contains(currentUserEmail)) {
            val updatedMembers = team.members.toMutableList()
            updatedMembers.add(currentUserEmail)

            firestore.collection("teams").document(team.id)
                .update("members", updatedMembers)
                .addOnSuccessListener {
                    Toast.makeText(this, "Joined team successfully", Toast.LENGTH_SHORT).show()
                    loadTeams()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to join team", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Already a member of this team", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openTaskAssignment(team: Team) {
        val intent = Intent(this, TaskAssignmentActivity::class.java)
        intent.putExtra("teamId", team.id)
        startActivity(intent)
    }
}
