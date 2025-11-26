using System;
using System.Collections.Generic;

namespace GymMaster.API.DTOs
{
    // --- 1. DTO cho Nhóm cơ ---
    public class MuscleGroupDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string ImageUrl { get; set; }
    }

    // --- 2. DTO cho Bài tập (Cơ sở) ---
    public class ExerciseDTO
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string VideoUrl { get; set; }
        public string ImageUrl { get; set; }
        public int MuscleGroupId { get; set; }
    }

    // --- 3. DTO cho Chi tiết bước tập ---
    public class ExerciseStepDTO
    {
        public int Id { get; set; }
        public string ImageUrl { get; set; }
        public int StepOrder { get; set; }
        public string Description { get; set; }

        // Trường Dữ liệu Khớp với Projection (ExercisesController)
        public int StepNumber { get; set; }
        public string Title { get; set; }
        public string Detail { get; set; }
    }

    // --- 4. DTO Chi tiết bài tập (Tên cuối cùng đã sửa) ---
    public class ExerciseDetailsResponse : ExerciseDTO // Hoặc ExerciseDetailDTO nếu bạn đã sửa tên nó đồng bộ
    {
        public string MuscleGroup { get; set; }
        public List<ExerciseStepDTO> Steps { get; set; }
    }

    // ----------------------------------------------------
    // --- 5. DTO REQUEST/RESPONSE KHÁC ---
    // ----------------------------------------------------

    // Auth
    public class LoginReq
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }

    public class RegisterReq
    {
        public string FullName { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string PhoneNumber { get; set; }
    }

    // Profile
    public class ProfileDTO
    {
        public int Id { get; set; }
        public string FullName { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public string RoleName { get; set; }
    }

    public class UpdateProfileReq
    {
        public string FullName { get; set; }
        public string PhoneNumber { get; set; }
    }

    // Trainer & Workout Logs & Booking
    public class TrainerDTO
    {
        public int Id { get; set; }
        public string FullName { get; set; }
        public string Email { get; set; }
        public string Specialization { get; set; }
        public string Bio { get; set; }
        public string AvatarUrl { get; set; }
        public decimal HourlyRate { get; set; }
    }

    public class WorkoutLogDTO
    {
        public int Id { get; set; }
        public System.DateTime? DateLogged { get; set; }
        public int? DurationMinutes { get; set; }
        public int? CaloriesBurned { get; set; }
        public string Notes { get; set; }
    }

    public class AddWorkoutLogReq
    {
        public int DurationMinutes { get; set; }
        public int CaloriesBurned { get; set; }
        public string Notes { get; set; }
    }

    public class BookingReq
    {
        public int TrainerId { get; set; }
        public System.DateTime AppointmentTime { get; set; }
    }
}