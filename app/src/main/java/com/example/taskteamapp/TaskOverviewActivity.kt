package com.example.taskteamapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.taskteamapp.databinding.ActivityTaskoverviewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TaskOverviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskoverviewBinding
    private lateinit var teamId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskoverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the team ID from the intent
        teamId = intent.getStringExtra("teamId") ?: throw IllegalArgumentException("Team ID must be provided")

        setupViewPager()
    }

    private fun setupViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        // Setting the adapter for ViewPager2
        viewPager.adapter = TaskPagerAdapter(this, teamId)

        // Setting up TabLayout with ViewPager2
        val tabTitles = listOf("Ongoing", "Completed", "Upcoming")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    inner class TaskPagerAdapter(activity: AppCompatActivity, private val teamId: String) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3 // Total number of fragments

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OngoingTasksFragment.newInstance(teamId) // Ongoing tasks fragment
                1 -> CompletedTasksFragment.newInstance(teamId) // Completed tasks fragment
                else -> UpcomingTasksFragment.newInstance(teamId) // Upcoming tasks fragment
            }
        }
    }
}
