package com.example.newdogtracker.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.newdogtracker.R
import com.example.newdogtracker.registration.Login

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Login::class.java))
            finish() // Close SplashActivity after navigating
        }, 3000) // Delay in milliseconds (3 seconds)
    }

    override fun onPause() {
        super.onPause()
        finish() // Ensures the activity is closed when paused
    }
}
