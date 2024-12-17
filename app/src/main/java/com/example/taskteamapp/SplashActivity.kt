package com.example.taskteamapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Check if the user is logged in
        val isLoggedIn = checkUserLoggedIn()

        Handler(Looper.getMainLooper()).postDelayed({
            // Determine which activity to start
            val intent = if (isLoggedIn) {
                Intent(this, MainActivity::class.java) // User is logged in
            } else {
                Intent(this, LoginActivity::class.java) // User is not logged in
            }
            startActivity(intent)
            // Finish SplashActivity so the user can't navigate back to it
            finish()
        }, 3000L)
    }

    private fun checkUserLoggedIn(): Boolean {
        // Replace with your actual implementation for checking user login status
        val sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
}
