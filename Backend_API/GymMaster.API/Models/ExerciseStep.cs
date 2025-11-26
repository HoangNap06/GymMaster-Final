using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class ExerciseStep
{
    public int Id { get; set; }

    public int ExerciseId { get; set; }

    public int StepOrder { get; set; }

    public string? Description { get; set; }

    public string? ImageUrl { get; set; }

    public virtual Exercise Exercise { get; set; } = null!;
    // ✅ BỔ SUNG THUỘC TÍNH BỊ THIẾU
    public int StepNumber { get; set; }

    // ✅ BỔ SUNG CÁC THUỘC TÍNH CẦN THIẾT KHÁC (Title, Detail)
    // Dựa trên code ExercisesController bạn đã gửi:
    public string Title { get; set; }
    public string Detail { get; set; }

    
}
