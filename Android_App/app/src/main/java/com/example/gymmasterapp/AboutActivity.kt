package com.example.gymmasterapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Thiết lập sự kiện bấm cho nút liên hệ
        findViewById<Button>(R.id.btnContactEmail).setOnClickListener {
            sendEmail()
        }
        findViewById<Button>(R.id.btnContactPhone).setOnClickListener {
            dialPhone()
        }
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Chỉ ứng dụng email mới xử lý
            putExtra(Intent.EXTRA_EMAIL, arrayOf("naphoang@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Yêu cầu Hỗ trợ từ Ứng dụng GymMaster")
        }
        try {
            startActivity(Intent.createChooser(intent, "Gửi email bằng..."))
        } catch (e: Exception) {
            Toast.makeText(this, "Không tìm thấy ứng dụng email nào.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dialPhone() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:0385925130")
        }
        startActivity(intent)
    }
}