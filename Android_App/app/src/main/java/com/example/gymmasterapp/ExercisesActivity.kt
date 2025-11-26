package com.example.gymmasterapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExercisesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EXERCISE_ID = "EXERCISE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        val muscleGroupId = intent.getIntExtra("MUSCLE_GROUP_ID", 0)
        val muscleGroupName = intent.getStringExtra("MUSCLE_GROUP_NAME")

        findViewById<TextView>(R.id.tvTitle).text = muscleGroupName ?: "Danh sách bài tập"

        val rvExercises = findViewById<RecyclerView>(R.id.rvExercises)
        rvExercises.layoutManager = LinearLayoutManager(this)

        ApiClient.getService(this).getExercisesByMuscleGroup(muscleGroupId).enqueue(object : Callback<List<ExerciseDTO>> {
            override fun onResponse(call: Call<List<ExerciseDTO>>, response: Response<List<ExerciseDTO>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    rvExercises.adapter = ExerciseAdapter(list) { exercise ->
                        val intent = Intent(this@ExercisesActivity, ExerciseDetailActivity::class.java)
                        intent.putExtra(EXTRA_EXERCISE_ID, exercise.id)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@ExercisesActivity, "Không có bài tập nào", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ExerciseDTO>>, t: Throwable) {
                Toast.makeText(this@ExercisesActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}