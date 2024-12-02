package com.example.newdogtracker.group

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newdogtracker.MainScreenActivity
import com.example.newdogtracker.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Creategp : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var create: Button? = null
    private var mcode: EditText? = null
    private var uname: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creategp)

        mAuth = FirebaseAuth.getInstance()
        create = findViewById<Button>(R.id.creategp)
        mcode = findViewById<EditText>(R.id.code)

        val firebaseUser: FirebaseUser? = mAuth?.currentUser
        if (firebaseUser != null) {
            val uid: String = firebaseUser.uid

            val userInfo: DatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(uid)
                .child("Name")

            userInfo.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    uname = dataSnapshot.getValue(String::class.java)
                    if (uname != null) {
                        Log.d("Creategp", "Fetched username: $uname")
                        Toast.makeText(applicationContext, uname, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Creategp", "Error fetching username: " + databaseError.message)
                }
            })

            create?.setOnClickListener { v: View? ->
                val code = mcode?.text.toString().trim { it <= ' ' }
                if (code.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "Enter a valid group code!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                val groupRef: DatabaseReference = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Groups")
                    .child(code)
                    .child(uid)

                val newPost: MutableMap<String, String?> = HashMap()
                newPost["Name"] = uname
                groupRef.setValue(newPost).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userGroupRef: DatabaseReference = FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Users")
                            .child(uid)
                            .child("Group")

                        userGroupRef.setValue(code).addOnCompleteListener { groupTask ->
                            if (groupTask.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Group Created",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@Creategp,
                                    MainScreenActivity::class.java
                                )
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Failed to save group reference!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Failed to create group!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "User not authenticated!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
