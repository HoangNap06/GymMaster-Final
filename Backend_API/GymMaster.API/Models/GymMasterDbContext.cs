using System;
using Microsoft.EntityFrameworkCore;
using GymMaster.API.Models;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class GymMasterDbContext : DbContext
{
    public GymMasterDbContext() { }

    public GymMasterDbContext(DbContextOptions<GymMasterDbContext> options) : base(options) { }

    // Các DbSet giữ nguyên...
    public virtual DbSet<Booking> Bookings { get; set; }
    public virtual DbSet<Exercise> Exercises { get; set; }
    public virtual DbSet<ExerciseStep> ExerciseSteps { get; set; }
    public virtual DbSet<GymPackage> GymPackages { get; set; }
    public virtual DbSet<MuscleGroup> MuscleGroups { get; set; }
    public virtual DbSet<Role> Roles { get; set; }
    public virtual DbSet<Subscription> Subscriptions { get; set; }
    public virtual DbSet<Trainer> Trainers { get; set; }
    public virtual DbSet<User> Users { get; set; }
    public virtual DbSet<WorkoutLog> WorkoutLogs { get; set; }

    // ==========================================
    // ✅ THÊM DBSET CHO WORKOUT PLANNER
    // ==========================================
    public virtual DbSet<Workout> Workouts { get; set; }
    public virtual DbSet<WorkoutDay> WorkoutDays { get; set; }
    public virtual DbSet<WorkoutExercise> WorkoutExercises { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        // ✅ BỔ SUNG: Seed Data cho Roles
        modelBuilder.Entity<Role>().HasData(
            new Role { Id = 1, RoleName = "Admin" },
            new Role { Id = 2, RoleName = "Trainer" },
            new Role { Id = 3, RoleName = "Member" }
        );

        // ==========================================
        // CẤU HÌNH CÁC BẢNG WORKOUT MỚI
        // ==========================================

        // Bảng Workout (Kế hoạch chính)
        modelBuilder.Entity<Workout>(entity =>
        {
            // Liên kết với User (người tạo/sở hữu Workout)
            entity.HasOne(d => d.User)
                .WithMany()
                .HasForeignKey(d => d.UserId)
                .OnDelete(DeleteBehavior.Restrict);
        });

        // Bảng WorkoutDay (Ngày tập)
        modelBuilder.Entity<WorkoutDay>(entity =>
        {
            // Liên kết với Workout (kế hoạch cha)
            entity.HasOne(d => d.Workout)
                .WithMany(p => p.WorkoutDays)
                .HasForeignKey(d => d.WorkoutId)
                .OnDelete(DeleteBehavior.Cascade);
        });

        // Bảng WorkoutExercise (Bài tập chi tiết trong ngày)
        modelBuilder.Entity<WorkoutExercise>(entity =>
        {
            // Liên kết với WorkoutDay (ngày tập cha)
            entity.HasOne(d => d.WorkoutDay)
                .WithMany(p => p.WorkoutExercises)
                .HasForeignKey(d => d.WorkoutDayId)
                .OnDelete(DeleteBehavior.Cascade);

            // Liên kết với Exercise (bài tập gốc)
            entity.HasOne(d => d.Exercise)
                .WithMany()
                .HasForeignKey(d => d.ExerciseId)
                .OnDelete(DeleteBehavior.Restrict);
        });

        // ==========================================
        // CẤU HÌNH CÁC BẢNG CŨ (Giữ nguyên cấu hình ban đầu của bạn)
        // ==========================================

        modelBuilder.Entity<Booking>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__Bookings__3214EC0746F966B8");
            entity.HasOne(d => d.Member).WithMany(p => p.Bookings)
                .HasForeignKey(d => d.MemberId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__Bookings__Member__4F7CD00D");
            entity.HasOne(d => d.Trainer).WithMany(p => p.Bookings)
                .HasForeignKey(d => d.TrainerId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__Bookings__Traine__5070F446");
        });

        // Thêm các cấu hình cũ khác nếu cần (Ví dụ: Exercise, Subscription, etc.)
        // Do cấu hình cũ của bạn đang bị thiếu, tôi chỉ giữ lại Booking để đảm bảo biên dịch.

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}