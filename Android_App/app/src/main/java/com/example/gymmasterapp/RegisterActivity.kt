package com.example.gymmasterapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtRegName: EditText
    private lateinit var edtRegEmail: EditText
    private lateinit var edtRegPhone: EditText
    private lateinit var edtRegPass: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // 1. Ánh xạ View
        edtRegName = findViewById(R.id.edtRegName)
        edtRegEmail = findViewById(R.id.edtRegEmail)
        edtRegPhone = findViewById(R.id.edtRegPhone)
        edtRegPass = findViewById(R.id.edtRegPass)
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginLink = findViewById(R.id.tvLoginLink)

        // 2. Chuyển về màn hình đăng nhập
        tvLoginLink.setOnClickListener {
            finish()
        }

        // 3. Xử lý Đăng ký
        btnRegister.setOnClickListener {
            val name = edtRegName.text.toString().trim()
            val email = edtRegEmail.text.toString().trim()
            val phone = edtRegPhone.text.toString().trim()
            val pass = edtRegPass.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val req = RegisterReq(name, email, pass, phone)
            registerUser(req)
        }
    }

    private fun registerUser(req: RegisterReq) {
        // --- SỬ DỤNG API CLIENT ---
        val apiService = ApiClient.getService(this)

        apiService.registerUser(req).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Đăng ký thành công! Hãy đăng nhập.", Toast.LENGTH_LONG).show()
                    finish() // Đóng màn hình đăng ký để quay lại Login
                } else {
                    Toast.makeText(this@RegisterActivity, "Đăng ký thất bại: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}