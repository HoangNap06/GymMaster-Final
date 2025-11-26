package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.DecimalFormat

// Adapter hiển thị danh sách PT và xử lý sự kiện Đặt lịch
class TrainerAdapter(
    private val trainers: List<TrainerDTO>,
    private val onBookClick: (TrainerDTO) -> Unit // Callback khi bấm nút Đặt lịch
) : RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trainer, parent, false)
        return TrainerViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainerViewHolder, position: Int) {
        val trainer = trainers[position]

        // 1. Gán dữ liệu vào các View
        // Các biến này sẽ KHÔNG CÒN LỖI vì đã được khai báo đầy đủ trong ViewHolder bên dưới
        holder.tvName.text = trainer.fullName
        holder.tvSpecialization.text = trainer.specialization
        holder.tvBio.text = trainer.bio

        // Format tiền tệ (Ví dụ: 200,000 VNĐ/giờ)
        val formatter = DecimalFormat("#,###")
        holder.tvRate.text = "Giá: ${formatter.format(trainer.hourlyRate)} VNĐ/giờ"

        // 2. Load ảnh đại diện bằng Glide
        // Dùng context từ itemView để load ảnh
        Glide.with(holder.itemView.context)
            .load(trainer.avatarUrl)
            .placeholder(R.mipmap.ic_launcher) // Ảnh chờ khi đang tải
            .error(R.mipmap.ic_launcher)       // Ảnh hiển thị nếu lỗi
            .into(holder.imgAvatar)

        // 3. Xử lý sự kiện bấm nút Đặt lịch
        // Gọi hàm callback được truyền từ Activity
        holder.btnBook.setOnClickListener {
            onBookClick(trainer)
        }
    }

    override fun getItemCount(): Int = trainers.size

    // --- KHAI BÁO VIEW HOLDER (QUAN TRỌNG) ---
    // Class này chứa ĐẦY ĐỦ 6 biến View khớp với ID trong file XML item_trainer.xml
    // Nếu thiếu bất kỳ biến nào ở đây, hàm onBindViewHolder ở trên sẽ báo lỗi đỏ.
    class TrainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvTrainerName)
        val tvSpecialization: TextView = itemView.findViewById(R.id.tvSpecialization) // Đã bổ sung
        val tvBio: TextView = itemView.findViewById(R.id.tvBio)                   // Đã bổ sung
        val tvRate: TextView = itemView.findViewById(R.id.tvRate)                 // Đã bổ sung
        val imgAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)          // Đã bổ sung
        val btnBook: Button = itemView.findViewById(R.id.btnBook)                 // Đã bổ sung nút đặt lịch
    }
}