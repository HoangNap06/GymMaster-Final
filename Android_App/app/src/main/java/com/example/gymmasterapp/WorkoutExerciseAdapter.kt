package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutExerciseAdapter(
    private val exercises: List<WorkoutExerciseDTO>,
    private val onClick: (WorkoutExerciseDTO) -> Unit
) : RecyclerView.Adapter<WorkoutExerciseAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvExName: TextView = view.findViewById(R.id.tvExName)
        val tvExSetsReps: TextView = view.findViewById(R.id.tvExSetsReps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = exercises[position]

        holder.tvExName.text = "${position + 1}. ${item.exerciseName}"

        // Hiển thị Sets x Reps
        holder.tvExSetsReps.text = "${item.setCount} Hiệp x ${item.repsPerSet} Lần"

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = exercises.size
}