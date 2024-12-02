package com.example.newdogtracker

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newdogtracker.group.Creategp
import com.example.newdogtracker.group.Joingp
import com.example.newdogtracker.registration.Login
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var createGp: Button
    private lateinit var joinGp: Button
    private lateinit var goToMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize views
        createGp = findViewById(R.id.create)
        joinGp = findViewById(R.id.join)
        goToMain = findViewById(R.id.button2)

        // Set click listeners
        createGp.setOnClickListener(this)
        joinGp.setOnClickListener(this)
        goToMain.setOnClickListener {
            val intent = Intent(this, MainScreenActivity::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        mAuth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser == null) {
                val intent = Intent(this@HomeActivity, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.create -> {
                val intent = Intent(this, Creategp::class.java)
                startActivity(intent)
            }
            R.id.join -> {
                val intent = Intent(this, Joingp::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(1, 1, 1, "Settings")
        menu.add(1, 2, 2, "Log Out")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> {
                // TODO: Navigate to settings
            }
            2 -> {
                mAuth.signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
