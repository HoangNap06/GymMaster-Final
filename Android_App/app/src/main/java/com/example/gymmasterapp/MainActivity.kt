package com.example.gymmasterapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Ánh xạ View
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegisterLink)

        // 2. Cấu hình Retrofit qua ApiClient
        val apiService = ApiClient.getService(this)

        // 3. Xử lý sự kiện bấm nút ĐĂNG NHẬP
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val pass = edtPassword.text.toString().trim()

            // Kiểm tra nhập liệu
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ email và mật khẩu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gọi API Đăng nhập
            val req = LoginRequest(email, pass)
            apiService.loginUser(req).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val user = response.body()

                        if (user != null) {
                            // A. Lưu Token và Tên vào SharedPreferences
                            val sharedPref = getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("JWT_TOKEN", user.token)
                                putString("USER_FULL_NAME", user.fullName)
                                apply()
                            }

                            // B. Thông báo thành công
                            Toast.makeText(this@MainActivity, "Xin chào ${user.fullName}!", Toast.LENGTH_SHORT).show()

                            // C. Chuyển sang màn hình Trang chủ (Home)
                            val intent = Intent(this@MainActivity, HomeActivity::class.java)

                            startActivity(intent)
                            finish()
                        }
                    } else {
                        // Xử lý lỗi từ server (401 Unauthorized)
                        Toast.makeText(this@MainActivity, "Đăng nhập thất bại! Sai email hoặc mật khẩu.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Lỗi kết nối (Server tắt, sai IP, không có mạng...)
                    Toast.makeText(this@MainActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        // 4. Xử lý sự kiện bấm nút ĐĂNG KÝ
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}