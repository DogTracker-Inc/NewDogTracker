package com.example.newdogtracker.adapters

import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.newdogtracker.R
import com.example.newdogtracker.model.Dog

class DogAdapter(private val dogs: List<Dog>) : RecyclerView.Adapter<DogAdapter.DogViewHolder>() {

    class DogViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        // Create a styled TextView for each item
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textSize = 20f // Change text size
            setTypeface(typeface, Typeface.BOLD) // Make text bold
            setPadding(16, 24, 16, 24) // Add padding around the text
            gravity = Gravity.START // Align text to the start
            setTextColor(ContextCompat.getColor(context, R.color.black)) // Set text color
            background = ContextCompat.getDrawable(context, R.drawable.recycler_item_background) // Optional background
        }
        return DogViewHolder(textView)
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val dog = dogs[position]
        holder.textView.text = "Name: ${dog.name}\nAge: ${dog.age}\nBreed: ${dog.breed}"
    }

    override fun getItemCount(): Int = dogs.size
}
