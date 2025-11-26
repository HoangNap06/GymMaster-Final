package com.example.gymmasterapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

// Thêm tham số onBuyClick vào constructor
class PackageAdapter(
    private val packages: List<GymPackage>,
    private val onBuyClick: (GymPackage) -> Unit // Callback khi bấm nút Mua
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_package, parent, false)
        return PackageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        val pkg = packages[position]

        // 1. Gán Tên gói
        holder.tvName.text = pkg.packageName

        // 2. Gán Mô tả (BỔ SUNG)
        holder.tvDescription.text = pkg.description ?: "Không có mô tả chi tiết." // Xử lý nếu mô tả NULL

        // 3. Gán Thời hạn
        holder.tvDuration.text = "${pkg.durationInDays} Ngày"

        // 4. Format và gán Giá
        val formatter = DecimalFormat("#,###")
        holder.tvPrice.text = "${formatter.format(pkg.price)} VND"

        // 5. Xử lý sự kiện bấm nút Đăng ký
        holder.btnBuy.setOnClickListener {
            onBuyClick(pkg)
        }
    }

    override fun getItemCount(): Int = packages.size

    class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvPackageName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription) // <--- Ánh xạ mới
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvDuration: TextView = itemView.findViewById(R.id.tvDuration)
        val btnBuy: Button = itemView.findViewById(R.id.btnBuy)
    }
}