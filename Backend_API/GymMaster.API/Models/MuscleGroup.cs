using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class MuscleGroup
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? ImageUrl { get; set; }

    public virtual ICollection<Exercise> Exercises { get; set; } = new List<Exercise>();
}
