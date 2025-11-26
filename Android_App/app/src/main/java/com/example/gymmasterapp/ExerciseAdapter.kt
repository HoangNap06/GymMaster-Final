package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ExerciseAdapter(
    private val exercises: List<ExerciseDTO>,
    private val onClick: (ExerciseDTO) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgExercise: ImageView = view.findViewById(R.id.imgExercise)
        val tvExerciseName: TextView = view.findViewById(R.id.tvExerciseName)
        val tvExerciseDesc: TextView = view.findViewById(R.id.tvExerciseDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = exercises[position]
        holder.tvExerciseName.text = item.name
        holder.tvExerciseDesc.text = item.description

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.imgExercise)

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = exercises.size
}