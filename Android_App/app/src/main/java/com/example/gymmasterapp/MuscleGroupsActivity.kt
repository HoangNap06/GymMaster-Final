package com.example.gymmasterapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MuscleGroupsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_muscle_groups)

        val rvMuscleGroups = findViewById<RecyclerView>(R.id.rvMuscleGroups)
        rvMuscleGroups.layoutManager = GridLayoutManager(this, 2) // Grid 2 cột

        // --- SỬ DỤNG API CLIENT ---
        ApiClient.getService(this).getMuscleGroups().enqueue(object : Callback<List<MuscleGroupDTO>> {
            override fun onResponse(call: Call<List<MuscleGroupDTO>>, response: Response<List<MuscleGroupDTO>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()

                    // Khi bấm vào một nhóm cơ, chuyển sang màn hình danh sách bài tập của nhóm đó
                    rvMuscleGroups.adapter = MuscleGroupAdapter(list) { group ->
                        val intent = Intent(this@MuscleGroupsActivity, ExercisesActivity::class.java)
                        intent.putExtra("MUSCLE_GROUP_ID", group.id)
                        intent.putExtra("MUSCLE_GROUP_NAME", group.name)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@MuscleGroupsActivity, "Lỗi tải nhóm cơ", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<MuscleGroupDTO>>, t: Throwable) {
                Toast.makeText(this@MuscleGroupsActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }
}