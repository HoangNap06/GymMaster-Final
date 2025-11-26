package com.example.gymmasterapp

import android.content.Intent
import android.os.Bundle
import android.view.View // <<< Đã thêm import này >>>
import android.widget.LinearLayout
import android.widget.ProgressBar // <<< Đã thêm import này >>>
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutDetailActivity : AppCompatActivity() {

    private lateinit var tvWorkoutName: TextView
    private lateinit var tvWorkoutDescription: TextView
    private lateinit var layoutDayContainer: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        // Ánh xạ View
        tvWorkoutName = findViewById(R.id.tvWorkoutDetailName)
        tvWorkoutDescription = findViewById(R.id.tvWorkoutDetailDescription)
        layoutDayContainer = findViewById(R.id.layoutDayContainer)
        progressBar = findViewById(R.id.progressBar)

        // Lấy ID Workout từ Intent
        val workoutId = intent.getIntExtra("WORKOUT_ID", -1)
        if (workoutId != -1) {
            fetchWorkoutDetails(workoutId)
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID kế hoạch.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun fetchWorkoutDetails(id: Int) {
        progressBar.visibility = View.VISIBLE

        ApiClient.getService(this).getWorkoutDetails(id).enqueue(object : Callback<WorkoutDetailResponse> {
            override fun onResponse(call: Call<WorkoutDetailResponse>, response: Response<WorkoutDetailResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let { details ->
                        bindData(details)
                    }
                } else {
                    Toast.makeText(this@WorkoutDetailActivity, "Lỗi tải chi tiết: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WorkoutDetailResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@WorkoutDetailActivity, "Lỗi mạng khi tải chi tiết.", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun bindData(details: WorkoutDetailResponse) {
        tvWorkoutName.text = details.name
        tvWorkoutDescription.text = details.description

        // Vòng lặp qua từng ngày tập (Thứ 2, Thứ 3,...)
        details.days.forEach { day ->
            // Sử dụng item_workout_day.xml để hiển thị từng ngày
            val dayView = layoutInflater.inflate(R.layout.item_workout_day, layoutDayContainer, false)
            val tvDayFocus = dayView.findViewById<TextView>(R.id.tvDayFocus)
            val rvExercises = dayView.findViewById<RecyclerView>(R.id.rvDayExercises)

            // Chuyển DayOrder (1, 2,...) thành Tên Ngày (Thứ Hai, Thứ Ba,...)
            val dayName = when(day.dayOrder) {
                1 -> "Thứ Hai"
                2 -> "Thứ Ba"
                3 -> "Thứ Tư"
                4 -> "Thứ Năm"
                5 -> "Thứ Sáu"
                6 -> "Thứ Bảy"
                7 -> "Chủ Nhật"
                else -> "Ngày Tập"
            }
            tvDayFocus.text = "$dayName: ${day.focus}"

            // Gắn Adapter cho danh sách bài tập trong ngày đó
            rvExercises.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

            // Adapter cho từng bài tập chi tiết (Set x Reps)
            rvExercises.adapter = WorkoutExerciseAdapter(day.exercises) { exercise ->
                // Khi bấm vào bài tập chi tiết, mở màn hình ExerciseDetailActivity
                val intent = Intent(this, ExerciseDetailActivity::class.java)
                intent.putExtra(ExerciseDetailActivity.EXTRA_EXERCISE_ID, exercise.exerciseId)
                startActivity(intent)
            }

            layoutDayContainer.addView(dayView)
        }
    }
}