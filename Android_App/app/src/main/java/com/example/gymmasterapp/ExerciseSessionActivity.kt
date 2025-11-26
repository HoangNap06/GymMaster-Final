package com.example.gymmasterapp

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseSessionActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var btnFinish: Button
    private lateinit var tvStatus: TextView

    private val totalTime = 60000L // 60 giây demo
    private var timer: CountDownTimer? = null
    private var timeElapsedSeconds = 0 // Biến đếm thời gian thực tế đã trôi qua

    private var currentExerciseDetail: ExerciseDetailDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_session)

        tvTimer = findViewById(R.id.tvTimer)
        btnFinish = findViewById(R.id.btnFinishSession)
        tvStatus = findViewById(R.id.tvSessionStatus)

        currentExerciseDetail = intent.getSerializableExtra(ExerciseDetailActivity.EXTRA_EXERCISE_DETAIL) as? ExerciseDetailDTO

        if (currentExerciseDetail != null) {
            tvStatus.text = "Đang tập: ${currentExerciseDetail!!.name}"
            startTimer()
        } else {
            tvStatus.text = "Lỗi: Không tải được dữ liệu bài tập"
        }

        btnFinish.setOnClickListener {
            timer?.cancel()
            // Dòng này rất quan trọng: Gọi hàm lưu log khi bấm HOÀN THÀNH
            saveWorkoutLog(currentExerciseDetail)
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Tính thời gian đã trôi qua (Total - Remaining)
                timeElapsedSeconds = ((totalTime - millisUntilFinished) / 1000).toInt()
                val secondsRemaining = millisUntilFinished / 1000
                tvTimer.text = String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60)
            }

            override fun onFinish() {
                // Thời gian trôi qua là toàn bộ thời gian
                timeElapsedSeconds = (totalTime / 1000).toInt()
                tvTimer.text = "00:00"
                saveWorkoutLog(currentExerciseDetail) // Tự động lưu khi hết giờ
            }
        }.start()
    }

    private fun saveWorkoutLog(detail: ExerciseDetailDTO?) {
        if (detail == null) {
            Toast.makeText(this, "Không thể lưu log: Thiếu thông tin bài tập.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Tính thời gian tập thực tế (chuyển sang phút, đảm bảo ít nhất 1 phút)
        val durationMinutes = if ((timeElapsedSeconds / 60) < 1) 1 else (timeElapsedSeconds / 60)

        // Logic tính Calo giả định: 8 Calo/phút
        val caloriesBurned = durationMinutes * 8

        val notes = "Hoàn thành ${detail.name} trong ${durationMinutes} phút."

        val req = AddWorkoutLogReq(
            durationMinutes = durationMinutes,
            caloriesBurned = caloriesBurned,
            notes = notes
        )

        // Gọi API POST /api/WorkoutLogs
        ApiClient.getService(this).addWorkoutLog(req).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ExerciseSessionActivity, "Đã lưu log: ${durationMinutes} phút, ${caloriesBurned} Calo!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@ExerciseSessionActivity, "Lưu log thất bại: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                finish() // Đóng màn hình phiên tập
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@ExerciseSessionActivity, "Lỗi mạng khi lưu log: ${t.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}