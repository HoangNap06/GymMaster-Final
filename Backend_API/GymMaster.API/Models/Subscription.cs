using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class Subscription
{
    public int Id { get; set; }

    public int UserId { get; set; }

    public int PackageId { get; set; }

    public DateTime? StartDate { get; set; }

    public DateTime EndDate { get; set; }

    public string? Status { get; set; }

    public virtual GymPackage Package { get; set; } = null!;

    public virtual User User { get; set; } = null!;
}
