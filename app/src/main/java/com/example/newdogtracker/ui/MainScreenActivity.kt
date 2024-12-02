package com.example.newdogtracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newdogtracker.adapters.DogAdapter
import com.example.newdogtracker.map.MapLoc
import com.example.newdogtracker.model.Dog

class MainScreenActivity : AppCompatActivity() {

    private lateinit var dogRecyclerView: RecyclerView
    private lateinit var dogAdapter: DogAdapter
    private val dogList = mutableListOf<Dog>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        dogRecyclerView = findViewById(R.id.dogRecyclerView)
        dogAdapter = DogAdapter(dogList)
        dogRecyclerView.layoutManager = LinearLayoutManager(this)
        dogRecyclerView.adapter = dogAdapter

        val nameEditText = findViewById<EditText>(R.id.dogNameInput)
        val ageEditText = findViewById<EditText>(R.id.dogAgeInput)
        val breedEditText = findViewById<EditText>(R.id.dogBreedInput)
        val addButton = findViewById<Button>(R.id.addDogButton)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull() ?: return@setOnClickListener
            val breed = breedEditText.text.toString()

            if (name.isNotEmpty() && breed.isNotEmpty()) {
                val newDog = Dog(name, age, breed)
                dogList.add(newDog)
                dogAdapter.notifyItemInserted(dogList.size - 1)

                // Clear input fields
                nameEditText.text.clear()
                ageEditText.text.clear()
                breedEditText.text.clear()
            }
        }

        val goToMapButton = findViewById<Button>(R.id.goToMapButton)
        goToMapButton.setOnClickListener {
            val intent = Intent(this, MapLoc::class.java)
            startActivity(intent)
        }
    }
}
