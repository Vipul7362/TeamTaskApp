package com.example.taskteamapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskteamapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Set up sign up button click listener
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (validateInput(name, email, phone, password, confirmPassword)) {
                createUser(email, password)
            }
        }

        // Redirect to Login Activity
        binding.signupRedirectTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    // Validate input fields
    private fun validateInput(name: String, email: String, phone: String, password: String, confirmPassword: String): Boolean {
        if (name.isEmpty()) {
            binding.nameEditText.error = "Name is required"
            binding.nameEditText.requestFocus()
            return false
        }

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            binding.emailEditText.requestFocus()
            return false
        }

        if (phone.isEmpty()) {
            binding.phoneEditText.error = "Phone number is required"
            binding.phoneEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            binding.passwordEditText.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty() || confirmPassword != password) {
            binding.confirmPasswordEditText.error = "Passwords do not match"
            binding.confirmPasswordEditText.requestFocus()
            return false
        }

        return true
    }

    // Function to create a new user
    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign up successful, redirect to MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Failed to sign up: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
