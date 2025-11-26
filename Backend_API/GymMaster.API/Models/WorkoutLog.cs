using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class WorkoutLog
{
    public int Id { get; set; }

    public int UserId { get; set; }

    public DateTime? DateLogged { get; set; }

    public int? DurationMinutes { get; set; }

    public int? CaloriesBurned { get; set; }

    public string? Notes { get; set; }

    public virtual User User { get; set; } = null!;
}
