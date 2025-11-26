package com.example.gymmasterapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private lateinit var edtFullName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        edtFullName = findViewById(R.id.edtFullName)
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        btnUpdate = findViewById(R.id.btnUpdateProfile)

        // Tải thông tin khi mở màn hình
        loadProfile()

        btnUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun loadProfile() {
        // --- SỬ DỤNG API CLIENT ---
        ApiClient.getService(this).getProfile().enqueue(object : Callback<ProfileDTO> {
            override fun onResponse(call: Call<ProfileDTO>, response: Response<ProfileDTO>) {
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        edtFullName.setText(profile.fullName)
                        edtEmail.setText(profile.email)
                        edtEmail.isEnabled = false // Không cho sửa email
                        edtPhone.setText(profile.phoneNumber)
                    }
                } else {
                    Toast.makeText(this@ProfileActivity, "Lỗi tải Profile: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ProfileDTO>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateProfile() {
        val fullName = edtFullName.text.toString().trim()
        val phone = edtPhone.text.toString().trim()

        if (fullName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Thông tin không được để trống", Toast.LENGTH_SHORT).show()
            return
        }

        val req = UpdateProfileReq(fullName, phone)

        // --- SỬ DỤNG API CLIENT ---
        ApiClient.getService(this).updateProfile(req).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                    loadProfile() // Load lại để chắc chắn dữ liệu đồng bộ
                } else {
                    Toast.makeText(this@ProfileActivity, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}