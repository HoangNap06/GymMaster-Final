package com.example.gymmasterapp

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

// Interceptor này sẽ được gắn vào OkHttpClient để tự động thêm Token vào mọi request
class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Lấy Token đã lưu ra từ SharedPreferences
        val token = context.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
            .getString("JWT_TOKEN", null)

        // Lấy request gốc
        val originalRequest = chain.request()

        // Nếu có Token, tạo request mới và thêm header "Authorization"
        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token") // Chuẩn JWT là "Bearer [Token]"
                .build()
        } else {
            // Nếu không có Token (chưa đăng nhập), dùng request gốc
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}