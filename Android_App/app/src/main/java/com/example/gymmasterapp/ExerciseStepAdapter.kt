package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// ✅ KHÔNG CẦN IMPORT DTO NẾU CÙNG PACKAGE
// Nếu nó báo đỏ chữ ExerciseStepDTO, hãy nhấn Alt + Enter để Import

class ExerciseStepAdapter(private val steps: List<ExerciseStepDTO>) :
    RecyclerView.Adapter<ExerciseStepAdapter.StepViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_step, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = steps[position]

        // Dùng các trường dữ liệu mới trong DTO
        holder.tvStepOrder.text = "BƯỚC ${step.stepNumber}"

        // Kiểm tra null cho title và detail
        val title = step.title ?: ""
        val detail = step.detail ?: ""
        holder.tvStepDescription.text = if(detail.isNotEmpty()) "$title: $detail" else title

        // Load ảnh bằng Glide
        if (!step.imageUrl.isNullOrEmpty()) {
            holder.ivStepImage.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(step.imageUrl)
                .placeholder(R.mipmap.ic_launcher) // Ảnh chờ
                .error(R.mipmap.ic_launcher)       // Ảnh lỗi
                .into(holder.ivStepImage)
        } else {
            holder.ivStepImage.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = steps.size

    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepOrder: TextView = itemView.findViewById(R.id.tvStepOrder)
        val tvStepDescription: TextView = itemView.findViewById(R.id.tvStepDescription)
        val ivStepImage: ImageView = itemView.findViewById(R.id.ivStepImage)
    }
}