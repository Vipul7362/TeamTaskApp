package com.example.taskteamapp

data class Team(
    val id: String = "",
    val name: String = "",
    val members: List<String> = emptyList()
)
