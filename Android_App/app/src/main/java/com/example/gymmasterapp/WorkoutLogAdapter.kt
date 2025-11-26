package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutLogAdapter(private val logs: List<WorkoutLogDTO>) :
    RecyclerView.Adapter<WorkoutLogAdapter.LogViewHolder>() {

    // Định dạng ngày giờ chuẩn ISO 8601 từ Server (ví dụ: 2025-11-24T08:00:00.000)
    // Chữ 'T' và '.SSS' (mili giây) là quan trọng để khớp với định dạng Server SQL Server/C#
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

    // Định dạng hiển thị cho người dùng (Ví dụ: 24/11/2025 (08:00))
    private val outputFormat = SimpleDateFormat("dd/MM/yyyy (HH:mm)", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]

        // 1. Xử lý và định dạng ngày tháng (LOGIC QUAN TRỌNG)
        try {
            val date = inputFormat.parse(log.dateLogged)
            holder.tvDate.text = outputFormat.format(date)
        } catch (e: Exception) {
            // Fallback nếu không parse được (chỉ lấy 10 ký tự đầu là YYYY-MM-DD)
            holder.tvDate.text = log.dateLogged.take(10)
        }

        // 2. Hiển thị Thời gian và Calo
        holder.tvDuration.text = "${log.durationMinutes} phút"
        holder.tvCalories.text = "${log.caloriesBurned} Calo"

        // 3. Ghi chú (Mô tả chi tiết buổi tập)
        holder.tvNotes.text = log.notes ?: "Không có ghi chú chi tiết."
    }

    override fun getItemCount(): Int = logs.size

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val tvCalories: TextView = itemView.findViewById(R.id.tvCalories)
        val tvNotes: TextView = itemView.findViewById(R.id.tvNotes)
    }
}