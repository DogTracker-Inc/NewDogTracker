package com.example.newdogtracker.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newdogtracker.MainScreenActivity
import com.example.newdogtracker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class Joingp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var joinCodeField: EditText
    private lateinit var joinButton: Button
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joingp)

        joinCodeField = findViewById(R.id.jcode)
        joinButton = findViewById(R.id.join)
        auth = FirebaseAuth.getInstance()

        val firebaseUser: FirebaseUser? = auth.currentUser
        val userId = firebaseUser?.uid ?: return

        val userInfoRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("Name")
        userInfoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.getValue(String::class.java) ?: ""
                Log.d("Joingp", "User name fetched: $userName")
                Toast.makeText(applicationContext, userName, Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error if needed
            }
        })

        joinButton.setOnClickListener {
            val groupCode = joinCodeField.text.toString()
            if (groupCode.isNotEmpty()) {
                joinGroup(userId, groupCode)
            } else {
                Toast.makeText(applicationContext, "Please enter a group code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun joinGroup(userId: String, groupCode: String) {
        val groupRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(groupCode).child(userId)
        val newUser = hashMapOf<String, Any>("Name" to userName)
        groupRef.setValue(newUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
                userRef.child("Group").setValue(groupCode)

                Toast.makeText(applicationContext, "Group Joined", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Failed to join group", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
