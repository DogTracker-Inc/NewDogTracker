package com.example.newdogtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dogs")
data class Dog(
    @PrimaryKey(autoGenerate = true) val id: String = 0,
    val name: String,
    val age: String,
    val breed: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
