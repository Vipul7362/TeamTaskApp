package com.example.taskteamapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskteamapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var teamsRecyclerView: RecyclerView
    private lateinit var assignedTeamAdapter: AssignedTeamAdapter
    private lateinit var teams: MutableList<Team> // Changed to MutableList for flexibility

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView






        // Create Team Button
        binding.createTeamButton.setOnClickListener {
            startActivity(Intent(this, TeamActivity::class.java))
        }

        // Join Team Button
        binding.joinTeamButton.setOnClickListener {
            startActivity(Intent(this, TeamActivity::class.java))
        }
    }
}
