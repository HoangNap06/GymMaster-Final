package com.example.gymmasterapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    // 1. Đăng nhập
    @POST("api/Auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    // 2. Lấy danh sách Gói tập
    @GET("api/Packages")
    fun getPackages(): Call<List<GymPackage>>

    // 3. Lấy danh sách Huấn luyện viên
    @GET("api/Trainers")
    fun getTrainers(): Call<List<TrainerDTO>>

    // 4. Lấy Profile cá nhân
    @GET("api/Profile")
    fun getProfile(): Call<ProfileDTO>

    // 5. Cập nhật Profile cá nhân
    @PUT("api/Profile")
    fun updateProfile(@Body request: UpdateProfileReq): Call<Unit>

    // 6. Lấy lịch sử tập luyện
    @GET("api/WorkoutLogs")
    fun getWorkoutLogs(): Call<List<WorkoutLogDTO>>

    // 7. Thêm nhật ký tập luyện
    @POST("api/WorkoutLogs")
    fun addWorkoutLog(@Body request: AddWorkoutLogReq): Call<Unit>

    // 8. Đặt lịch với PT
    @POST("api/Bookings")
    fun bookAppointment(@Body request: BookingReq): Call<Unit>

    // 9. Đăng ký tài khoản mới
    @POST("api/Auth/register")
    fun registerUser(@Body request: RegisterReq): Call<Unit>

    // 10. Lấy danh sách Nhóm cơ
    @GET("api/MuscleGroups")
    fun getMuscleGroups(): Call<List<MuscleGroupDTO>>

    // 11. Lấy danh sách Bài tập theo Nhóm cơ
    @GET("api/Exercises/ByMuscleGroup/{muscleGroupId}")
    fun getExercisesByMuscleGroup(@retrofit2.http.Path("muscleGroupId") muscleGroupId: Int): Call<List<ExerciseDTO>>

    // 12. Lấy chi tiết Bài tập
    @GET("api/Exercises/{id}")
    fun getExerciseDetail(@retrofit2.http.Path("id") id: Int): Call<ExerciseDetailDTO>

    // 13. API Mua gói tập
    @POST("api/Subscriptions")
    fun subscribe(@Body packageId: Int): Call<Unit>

    // ===========================================
    // ✅ API MỚI CHO WORKOUT PLANNER (KẾ HOẠCH TẬP)
    // ===========================================

    // 14. Lấy danh sách các kế hoạch tập luyện
    @GET("api/Workouts")
    fun getWorkouts(): Call<List<WorkoutDTO>>

    // 15. Lấy chi tiết một kế hoạch tập (bao gồm các ngày và bài tập)
    @GET("api/Workouts/{id}/Details")
    fun getWorkoutDetails(@retrofit2.http.Path("id") id: Int): Call<WorkoutDetailResponse>
}