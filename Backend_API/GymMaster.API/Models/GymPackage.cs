using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class GymPackage
{
    public int Id { get; set; }

    public string PackageName { get; set; } = null!;

    public string? Description { get; set; }

    public decimal Price { get; set; }

    public int DurationInDays { get; set; }

    public bool? IsActive { get; set; }

    public virtual ICollection<Subscription> Subscriptions { get; set; } = new List<Subscription>();
}
