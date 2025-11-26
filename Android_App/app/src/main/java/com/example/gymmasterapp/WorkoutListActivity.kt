package com.example.gymmasterapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutListActivity : AppCompatActivity() {

    private lateinit var rvWorkouts: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dùng Layout danh sách kế hoạch
        setContentView(R.layout.activity_workout_list)

        // Ánh xạ View
        rvWorkouts = findViewById(R.id.rvWorkouts)
        progressBar = findViewById(R.id.progressBar)
        rvWorkouts.layoutManager = LinearLayoutManager(this)

        // Cập nhật tiêu đề màn hình
        findViewById<TextView>(R.id.tvTitle).text = "KẾ HOẠCH TẬP LUYỆN"

        fetchWorkouts()
    }

    private fun fetchWorkouts() {
        progressBar.visibility = View.VISIBLE

        // Gọi API để lấy danh sách các kế hoạch (API: /api/Workouts)
        ApiClient.getService(this).getWorkouts().enqueue(object : Callback<List<WorkoutDTO>> {
            override fun onResponse(call: Call<List<WorkoutDTO>>, response: Response<List<WorkoutDTO>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val workouts = response.body() ?: emptyList()

                    if (workouts.isEmpty()) {
                        // Nếu danh sách rỗng, lỗi có thể do Backend chưa có dữ liệu (chưa chạy SeedData)
                        Toast.makeText(this@WorkoutListActivity, "Chưa có kế hoạch tập luyện nào (Kiểm tra dữ liệu mẫu SQL).", Toast.LENGTH_LONG).show()
                        return
                    }

                    val adapter = WorkoutAdapter(workouts) { workout ->
                        // Chuyển sang màn hình chi tiết Workout
                        val intent = Intent(this@WorkoutListActivity, WorkoutDetailActivity::class.java)
                        intent.putExtra("WORKOUT_ID", workout.id) // Truyền ID kế hoạch qua Intent
                        startActivity(intent)
                    }
                    rvWorkouts.adapter = adapter

                } else {
                    Toast.makeText(this@WorkoutListActivity, "Lỗi tải kế hoạch: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WorkoutDTO>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@WorkoutListActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}