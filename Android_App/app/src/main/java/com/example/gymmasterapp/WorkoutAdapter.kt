package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WorkoutAdapter(
    private val workouts: List<WorkoutDTO>,
    private val onClick: (WorkoutDTO) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvWorkoutName: TextView = view.findViewById(R.id.tvWorkoutName)
        val tvWorkoutDesc: TextView = view.findViewById(R.id.tvWorkoutDesc)
        val tvWorkoutDays: TextView = view.findViewById(R.id.tvWorkoutDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = workouts[position]
        holder.tvWorkoutName.text = item.name
        holder.tvWorkoutDesc.text = item.description

        // Hiển thị số ngày tập trong kế hoạch
        holder.tvWorkoutDays.text = "${item.totalDays} Ngày"

        // Thẻ CUSTOM/MẪU
        val type = if (item.isCustom) "Tùy chỉnh" else "Mẫu"
        holder.tvWorkoutDays.append(" | Loại: $type")

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = workouts.size
}