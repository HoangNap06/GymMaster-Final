package com.example.gymmasterapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        val videoUrl = intent.getStringExtra("VIDEO_URL")
        videoView = findViewById(R.id.videoView)
        val btnClose = findViewById<TextView>(R.id.btnClose)
        progressBar = findViewById(R.id.progressBar)

        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, "Lỗi link video", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        try {
            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            val uri = Uri.parse(videoUrl)
            videoView.setVideoURI(uri)

            // Hiện Loading khi bắt đầu tải
            progressBar.visibility = View.VISIBLE

            videoView.setOnPreparedListener { mp ->
                // Ẩn Loading khi video đã sẵn sàng chạy
                progressBar.visibility = View.GONE
                mp.isLooping = true
                videoView.start()
            }

            // ✅ CODE SỬA LỖI: Xử lý lỗi tải chi tiết hơn
            videoView.setOnErrorListener { mp, what, extra ->
                progressBar.visibility = View.GONE

                var msg = "Lỗi không xác định"
                if (what == 1 && extra == -1004) {
                    msg = "Lỗi IO/Mạng (Không thể kết nối Server)"
                } else if (what == 1 && extra == -110) {
                    msg = "Hết thời gian chờ (Timeout)"
                }

                Toast.makeText(this, "Video Lỗi: $msg", Toast.LENGTH_LONG).show()
                true
            }

        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Lỗi hệ thống: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}