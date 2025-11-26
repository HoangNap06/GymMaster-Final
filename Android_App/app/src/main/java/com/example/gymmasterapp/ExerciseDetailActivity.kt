package com.example.gymmasterapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EXERCISE_ID = "EXERCISE_ID"
        const val EXTRA_EXERCISE_DETAIL = "EXTRA_EXERCISE_DETAIL"
    }

    private lateinit var tvDetailDescription: TextView
    private lateinit var videoView: VideoView
    private lateinit var stepsRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fabStartExercise: FloatingActionButton
    private lateinit var tvDetailSummary: TextView

    private var currentExerciseDetail: ExerciseDetailDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_detail)

        // Ánh xạ View
        tvDetailDescription = findViewById(R.id.text_exercise_description)
        videoView = findViewById(R.id.videoView)
        stepsRecyclerView = findViewById(R.id.recycler_view_steps)
        progressBar = findViewById(R.id.progressBar)
        fabStartExercise = findViewById(R.id.fab_start_exercise)
        tvDetailSummary = findViewById(R.id.text_exercise_summary)

        stepsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Thiết lập Toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        val exerciseId = intent.getIntExtra(EXTRA_EXERCISE_ID, -1)
        if (exerciseId != -1) {
            fetchExerciseDetail(exerciseId)
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID bài tập.", Toast.LENGTH_SHORT).show()
            finish()
        }

        fabStartExercise.setOnClickListener {
            startExerciseSession()
        }
    }

    private fun startExerciseSession() {
        val detail = currentExerciseDetail
        if (detail != null) {
            val intent = Intent(this, ExerciseSessionActivity::class.java).apply {
                putExtra(EXTRA_EXERCISE_DETAIL, detail)
            }
            startActivity(intent)
            Toast.makeText(this, "Bắt đầu bài tập: ${detail.name}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Chưa tải xong chi tiết bài tập.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchExerciseDetail(id: Int) {
        progressBar.visibility = View.VISIBLE
        fabStartExercise.visibility = View.GONE

        val call = ApiClient.getService(this).getExerciseDetail(id)

        call.enqueue(object : Callback<ExerciseDetailDTO> {
            override fun onResponse(
                call: Call<ExerciseDetailDTO>,
                response: Response<ExerciseDetailDTO>
            ) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let { detail ->
                        currentExerciseDetail = detail
                        bindData(detail)
                        fabStartExercise.visibility = View.VISIBLE
                    }
                } else {
                    Toast.makeText(
                        this@ExerciseDetailActivity,
                        "Lỗi tải chi tiết: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ExerciseDetailDTO>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@ExerciseDetailActivity,
                    "Lỗi kết nối mạng.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun bindData(detail: ExerciseDetailDTO) {
        findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar).title = detail.name
        tvDetailDescription.text = detail.description ?: "Không có mô tả."
        tvDetailSummary.text = "Nhóm cơ: ${detail.muscleGroup ?: "Chưa cập nhật"}"

        // --- FIX LOGIC VIDEO URL ---
        val rawUrl = detail.videoUrl
        if (!rawUrl.isNullOrEmpty()) {
            videoView.visibility = View.VISIBLE

            // Android Emulator không hiểu "localhost". Phải đổi thành "10.0.2.2"
            // Nếu chạy trên máy thật, bạn phải đổi thành IP LAN (VD: 192.168.1.x)
            val fixUrl = rawUrl.replace("localhost", "10.0.2.2")
                .replace("127.0.0.1", "10.0.2.2")

            val uri = Uri.parse(fixUrl)
            videoView.setVideoURI(uri)

            // Thêm trình điều khiển (Play/Pause/Seek)
            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            videoView.setOnPreparedListener { mp ->
                // Khi video đã sẵn sàng -> chạy luôn
                mp.isLooping = true
                videoView.start()
            }

            videoView.setOnErrorListener { _, _, _ ->
                Toast.makeText(this, "Không thể phát video: $fixUrl", Toast.LENGTH_LONG).show()
                true
            }
        } else {
            videoView.visibility = View.GONE
        }

        detail.steps?.let { steps ->
            stepsRecyclerView.adapter = ExerciseStepAdapter(steps)
        }
    }

    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}