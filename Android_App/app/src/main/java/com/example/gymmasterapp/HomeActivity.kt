package com.example.gymmasterapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {

    // Khai báo các CardView theo ID cũ (từ file XML GridLayout)
    private lateinit var txtWelcome: TextView
    private lateinit var cardPackages: CardView
    private lateinit var cardSchedule: CardView
    private lateinit var cardProfile: CardView
    private lateinit var cardHistory: CardView
    private lateinit var cardExercises: CardView
    private lateinit var cardAboutUs: CardView
    private lateinit var cardWorkouts: CardView // << KHAI BÁO MỚI CHO CHỨC NĂNG WORKOUT PLANNER >>
    private lateinit var cardLogout: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 1. Ánh xạ View
        txtWelcome = findViewById(R.id.txtWelcome)
        cardPackages = findViewById(R.id.cardPackages)
        cardSchedule = findViewById(R.id.cardSchedule)
        cardProfile = findViewById(R.id.cardProfile)
        cardHistory = findViewById(R.id.cardHistory)
        cardExercises = findViewById(R.id.cardExercises)
        cardAboutUs = findViewById(R.id.cardAboutUs)
        cardLogout = findViewById(R.id.cardLogout)
        cardWorkouts = findViewById(R.id.cardWorkouts) // << ÁNH XẠ CARD MỚI >>

        // 2. Hiển thị tên người dùng
        val sharedPref = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        val fullName = sharedPref.getString("USER_FULL_NAME", "Hội viên")
        txtWelcome.text = "Xin chào, ${fullName ?: "Hội viên"}"

        // 3. Thiết lập sự kiện bấm

        // -> GÓI TẬP
        cardPackages.setOnClickListener {
            startActivity(Intent(this, PackagesActivity::class.java))
        }

        // -> HUẤN LUYỆN VIÊN
        cardSchedule.setOnClickListener {
            startActivity(Intent(this, TrainersActivity::class.java))
        }

        // -> LÊN KẾ HOẠCH TẬP (MỚI)
        cardWorkouts.setOnClickListener {
            startActivity(Intent(this, WorkoutListActivity::class.java))
        }

        // -> PROFILE
        cardProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // -> LỊCH SỬ TẬP
        cardHistory.setOnClickListener {
            startActivity(Intent(this, WorkoutHistoryActivity::class.java))
        }

        // -> BÀI TẬP
        cardExercises.setOnClickListener {
            startActivity(Intent(this, MuscleGroupsActivity::class.java))
        }

        // -> GIỚI THIỆU & LIÊN HỆ
        cardAboutUs.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // -> ĐĂNG XUẤT
        cardLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}