package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MuscleGroupAdapter(
    private val muscleGroups: List<MuscleGroupDTO>,
    private val onClick: (MuscleGroupDTO) -> Unit
) : RecyclerView.Adapter<MuscleGroupAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgMuscleGroup: ImageView = view.findViewById(R.id.imgMuscleGroup)
        val tvMuscleGroupName: TextView = view.findViewById(R.id.tvMuscleGroupName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_muscle_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = muscleGroups[position]
        holder.tvMuscleGroupName.text = item.name

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.imgMuscleGroup)

        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = muscleGroups.size
}