package com.example.newdogtracker.map

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.newdogtracker.R
import com.example.newdogtracker.model.Dog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapLoc : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val dogList = mutableListOf<Dog>() // Simulated list of dogs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_loc)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync(this)
        } else {
            Toast.makeText(this, "Map fragment not found in layout", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.trackButton).setOnClickListener {
            addDogLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Enable location if permission granted
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        displayDogLocations()
    }

    private fun displayDogLocations() {
        dogList.forEach { dog ->
            val location = LatLng(dog.latitude, dog.longitude)
            mMap.addMarker(MarkerOptions().position(location).title(dog.name))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }

    private fun addDogLocation() {
        // Create a dialog to collect dog information from the user
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_dog, null)
        val builder = AlertDialog.Builder(this)
            .setTitle("Add Dog Location")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogView.findViewById<EditText>(R.id.editDogName).text.toString()
                val age = dialogView.findViewById<EditText>(R.id.editDogAge).text.toString().toIntOrNull() ?: 0
                val breed = dialogView.findViewById<EditText>(R.id.editDogBreed).text.toString()
                val latitude = dialogView.findViewById<EditText>(R.id.editLatitude).text.toString().toDoubleOrNull() ?: 0.0
                val longitude = dialogView.findViewById<EditText>(R.id.editLongitude).text.toString().toDoubleOrNull() ?: 0.0

                if (name.isNotEmpty() && breed.isNotEmpty()) {
                    val newDog = Dog(name, age, breed, latitude, longitude)
                    dogList.add(newDog)
                    displayDogLocations()
                    Toast.makeText(this, "$name's location added!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)

        builder.create().show()
    }
}
