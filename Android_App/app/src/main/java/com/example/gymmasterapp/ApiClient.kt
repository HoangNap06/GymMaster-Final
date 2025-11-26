package com.example.gymmasterapp

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // FIX CUỐI CÙNG: Chuyển sang HTTP và cổng 5114 để tránh lỗi chứng chỉ HTTPS và đồng bộ với Program.cs
    private const val BASE_URL = "http://10.0.2.2:5114/" // <-- CHUẨN XÁC: HTTP/5114

    // Biến lưu trữ instance để không tạo lại nhiều lần
    private var retrofit: Retrofit? = null

    fun getService(context: Context): ApiService {

        // Cần OkHttpClient để gắn Interceptor (Token)
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context)) // Tự động gắn Token
            .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!.create(ApiService::class.java)
    }
}