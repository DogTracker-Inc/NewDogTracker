package com.example.newdogtracker.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newdogtracker.HomeActivity
import com.example.newdogtracker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var phoneField: EditText
    private lateinit var nameField: EditText
    private lateinit var registerButton: Button
    private lateinit var cancelButton: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailField = findViewById(R.id.etemail)
        passwordField = findViewById(R.id.etPassword)
        registerButton = findViewById(R.id.btnSignUp)
        nameField = findViewById(R.id.etname)
        phoneField = findViewById(R.id.etmob)

        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    saveUserDetails(userId)
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveUserDetails(userId: String) {
        val name = nameField.text.toString()
        val phone = phoneField.text.toString()
        val email = auth.currentUser?.email ?: ""
        val group = "null"

        val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        val userDetails = mapOf(
            "Name" to name,
            "Phone" to phone,
            "Email" to email,
            "Group" to group
        )

        userRef.setValue(userDetails).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToHome()
            } else {
                Toast.makeText(this, "Failed to save user details", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            navigateToHome()
        }
    }
}
