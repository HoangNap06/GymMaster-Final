using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class Exercise
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public string? VideoUrl { get; set; }

    public string? ImageUrl { get; set; }

    public int MuscleGroupId { get; set; }

    public virtual ICollection<ExerciseStep> ExerciseSteps { get; set; } = new List<ExerciseStep>();

    public virtual MuscleGroup MuscleGroup { get; set; } = null!;
}
