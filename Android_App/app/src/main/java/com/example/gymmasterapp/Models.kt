package com.example.gymmasterapp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// ====================================================
// 1. DTOs cho TÍNH NĂNG AUTH
// ====================================================
data class LoginRequest(
    @SerializedName("Email", alternate = ["email"])
    val email: String,
    @SerializedName("Password", alternate = ["password"])
    val password: String
)

data class LoginResponse(
    @SerializedName("Message", alternate = ["message"])
    val message: String,
    @SerializedName("UserId", alternate = ["userId"])
    val userId: Int,
    @SerializedName("FullName", alternate = ["fullName"])
    val fullName: String,
    @SerializedName("Role", alternate = ["role"])
    val role: String,
    @SerializedName("Token", alternate = ["token"])
    val token: String
)

// ====================================================
// 2. DTOs cho TÍNH NĂNG GÓI TẬP
// ====================================================
data class GymPackage(
    @SerializedName("Id", alternate = ["id"])
    val id: Int,

    @SerializedName("PackageName", alternate = ["packageName"])
    val packageName: String,

    @SerializedName("Description", alternate = ["description"])
    val description: String?,

    @SerializedName("Price", alternate = ["price"])
    val price: Double,

    @SerializedName("DurationInDays", alternate = ["durationInDays"])
    val durationInDays: Int
)

// ====================================================
// 3. DTOs cho TÍNH NĂNG HUẤN LUYỆN VIÊN
// ====================================================
data class TrainerDTO(
    @SerializedName("Id", alternate = ["id"])
    val id: Int,

    @SerializedName("FullName", alternate = ["fullName"])
    val fullName: String,

    @SerializedName("Email", alternate = ["email"])
    val email: String,

    @SerializedName("Specialization", alternate = ["specialization"])
    val specialization: String?,

    @SerializedName("Bio", alternate = ["bio"])
    val bio: String?,

    @SerializedName("AvatarUrl", alternate = ["avatarUrl"])
    val avatarUrl: String?,

    @SerializedName("HourlyRate", alternate = ["hourlyRate"])
    val hourlyRate: Double
)

// ====================================================
// 4. DTOs cho TÍNH NĂNG PROFILE CÁ NHÂN
// ====================================================
data class ProfileDTO(
    @SerializedName("Id", alternate = ["id"])
    val id: Int,

    @SerializedName("FullName", alternate = ["fullName"])
    val fullName: String,

    @SerializedName("Email", alternate = ["email"])
    val email: String,

    @SerializedName("PhoneNumber", alternate = ["phoneNumber"])
    val phoneNumber: String?,

    @SerializedName("RoleName", alternate = ["roleName"])
    val roleName: String
)

data class UpdateProfileReq(
    @SerializedName("FullName", alternate = ["fullName"])
    val fullName: String,
    @SerializedName("PhoneNumber", alternate = ["phoneNumber"])
    val phoneNumber: String
)

// ====================================================
// 5. DTOs cho TÍNH NĂNG LỊCH SỬ TẬP LUYỆN
// ====================================================
data class WorkoutLogDTO(
    @SerializedName("Id", alternate = ["id"])
    val id: Int,
    @SerializedName("DateLogged", alternate = ["dateLogged"])
    val dateLogged: String,
    @SerializedName("DurationMinutes", alternate = ["durationMinutes"])
    val durationMinutes: Int,
    @SerializedName("CaloriesBurned", alternate = ["caloriesBurned"])
    val caloriesBurned: Int,
    @SerializedName("Notes", alternate = ["notes"])
    val notes: String?
)

data class AddWorkoutLogReq(
    @SerializedName("DurationMinutes", alternate = ["durationMinutes"])
    val durationMinutes: Int,
    @SerializedName("CaloriesBurned", alternate = ["caloriesBurned"])
    val caloriesBurned: Int,
    @SerializedName("Notes", alternate = ["notes"])
    val notes: String
)

data class BookingReq(
    @SerializedName("TrainerId", alternate = ["trainerId"])
    val trainerId: Int,
    @SerializedName("AppointmentTime", alternate = ["appointmentTime"])
    val appointmentTime: String
)

data class RegisterReq(
    @SerializedName("FullName", alternate = ["fullName"])
    val fullName: String,
    @SerializedName("Email", alternate = ["email"])
    val email: String,
    @SerializedName("Password", alternate = ["password"])
    val password: String,
    @SerializedName("PhoneNumber", alternate = ["phoneNumber"])
    val phoneNumber: String
)

data class MuscleGroupDTO(
    @SerializedName("Id", alternate = ["id"])
    val id: Int,
    @SerializedName("Name", alternate = ["name"])
    val name: String,
    @SerializedName("ImageUrl", alternate = ["imageUrl"])
    val imageUrl: String?
)

data class ExerciseDTO(
    @SerializedName("Id", alternate = ["id"])
    val id: Int,
    @SerializedName("Name", alternate = ["name"])
    val name: String,
    @SerializedName("Description", alternate = ["description"])
    val description: String?,
    @SerializedName("VideoUrl", alternate = ["videoUrl"])
    val videoUrl: String?,
    @SerializedName("ImageUrl", alternate = ["imageUrl"])
    val imageUrl: String?,
    @SerializedName("MuscleGroupId", alternate = ["muscleGroupId"])
    val muscleGroupId: Int
) : Serializable

data class ExerciseStepDTO(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("StepNumber")
    val stepNumber: Int,

    @SerializedName("Title")
    val title: String?,

    @SerializedName("Detail")
    val detail: String?,

    @SerializedName("ImageUrl")
    val imageUrl: String?
) : Serializable

data class ExerciseDetailDTO(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Description")
    val description: String?,

    @SerializedName("VideoUrl")
    val videoUrl: String?,

    @SerializedName("ImageUrl")
    val imageUrl: String?,

    @SerializedName("MuscleGroup")
    val muscleGroup: String?,

    @SerializedName("Steps")
    val steps: List<ExerciseStepDTO>?
) : Serializable

// ====================================================
// ✅ PHẦN 6: DTOs cho WORKOUT PLANNER (KẾ HOẠCH TẬP)
// ====================================================

// 6.1. DTO cho Danh sách Kế hoạch tập luyện (API: /api/Workouts)
data class WorkoutDTO(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("IsCustom")
    val isCustom: Boolean, // Kế hoạch do người dùng tự tạo hay là mẫu

    @SerializedName("TotalDays")
    val totalDays: Int // Tổng số ngày tập trong kế hoạch
) : Serializable

// 6.2. DTO cho Bài tập trong Kế hoạch (Để dùng trong WorkoutDayDTO)
data class WorkoutExerciseDTO(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("ExerciseId")
    val exerciseId: Int,

    @SerializedName("SetCount")
    val setCount: Int, // Số hiệp (Sets)

    @SerializedName("RepsPerSet")
    val repsPerSet: Int, // Số lần lặp (Reps)

    @SerializedName("Order")
    val order: Int, // Thứ tự bài tập trong ngày

    @SerializedName("ExerciseName")
    val exerciseName: String,

    @SerializedName("ExerciseVideoUrl")
    val exerciseVideoUrl: String?
) : Serializable

// 6.3. DTO cho Ngày tập trong Kế hoạch (Để dùng trong WorkoutDetailResponse)
data class WorkoutDayDTO(
    @SerializedName("DayOrder")
    val dayOrder: Int, // Thứ tự ngày trong tuần (1=Thứ 2)

    @SerializedName("Focus")
    val focus: String, // Nhóm cơ tập trung (VD: Chest & Triceps)

    @SerializedName("Exercises")
    val exercises: List<WorkoutExerciseDTO>
) : Serializable

// 6.4. DTO Chi tiết Kế hoạch (API: /api/Workouts/{id}/Details)
data class WorkoutDetailResponse(
    @SerializedName("Id")
    val id: Int,

    @SerializedName("Name")
    val name: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("Days")
    val days: List<WorkoutDayDTO>
) : Serializable