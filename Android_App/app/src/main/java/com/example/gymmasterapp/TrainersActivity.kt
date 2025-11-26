package com.example.gymmasterapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class TrainersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainers)

        val rvTrainers = findViewById<RecyclerView>(R.id.rvTrainers)
        rvTrainers.layoutManager = LinearLayoutManager(this)

        loadTrainers(rvTrainers)
    }

    private fun loadTrainers(recyclerView: RecyclerView) {
        // --- SỬ DỤNG API CLIENT ---
        ApiClient.getService(this).getTrainers().enqueue(object : Callback<List<TrainerDTO>> {
            override fun onResponse(call: Call<List<TrainerDTO>>, response: Response<List<TrainerDTO>>) {
                if (response.isSuccessful) {
                    val trainerList = response.body() ?: emptyList()

                    // Gán Adapter và truyền callback xử lý khi bấm nút "Đặt lịch"
                    val adapter = TrainerAdapter(trainerList) { selectedTrainer ->
                        showDatePicker(selectedTrainer)
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(this@TrainersActivity, "Không thể tải danh sách PT", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TrainerDTO>>, t: Throwable) {
                Toast.makeText(this@TrainersActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 1. Chọn ngày
    private fun showDatePicker(trainer: TrainerDTO) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            showTimePicker(trainer, selectedYear, selectedMonth, selectedDay)
        }, year, month, day)

        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    // 2. Chọn giờ
    private fun showTimePicker(trainer: TrainerDTO, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            // Format chuỗi theo chuẩn ISO 8601 (yyyy-MM-ddTHH:mm:ss) để gửi lên Backend
            val formattedMonth = String.format("%02d", month + 1)
            val formattedDay = String.format("%02d", day)
            val formattedHour = String.format("%02d", selectedHour)
            val formattedMinute = String.format("%02d", selectedMinute)

            val appointmentTime = "$year-$formattedMonth-${formattedDay}T$formattedHour:$formattedMinute:00"

            bookAppointment(trainer.id, appointmentTime)

        }, hour, minute, true).show()
    }

    // 3. Gọi API Đặt lịch
    private fun bookAppointment(trainerId: Int, appointmentTime: String) {
        val req = BookingReq(trainerId, appointmentTime)

        ApiClient.getService(this).bookAppointment(req).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@TrainersActivity, "Đặt lịch thành công!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@TrainersActivity, "Đặt lịch thất bại (Có thể trùng lịch)", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@TrainersActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}