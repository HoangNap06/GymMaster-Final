package com.example.gymmasterapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_history)

        val rvWorkoutLogs = findViewById<RecyclerView>(R.id.rvWorkoutLogs)
        rvWorkoutLogs.layoutManager = LinearLayoutManager(this)

        // --- SỬ DỤNG API CLIENT ---
        ApiClient.getService(this).getWorkoutLogs().enqueue(object : Callback<List<WorkoutLogDTO>> {
            override fun onResponse(call: Call<List<WorkoutLogDTO>>, response: Response<List<WorkoutLogDTO>>) {
                if (response.isSuccessful) {
                    val logs = response.body() ?: emptyList()
                    // Gắn Adapter
                    val adapter = WorkoutLogAdapter(logs)
                    rvWorkoutLogs.adapter = adapter
                } else {
                    Toast.makeText(this@WorkoutHistoryActivity, "Lỗi tải dữ liệu: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<WorkoutLogDTO>>, t: Throwable) {
                Toast.makeText(this@WorkoutHistoryActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}