using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class Trainer
{
    public int Id { get; set; }

    public int UserId { get; set; }

    public string? Specialization { get; set; }

    public string? Bio { get; set; }

    public decimal? HourlyRate { get; set; }

    public bool? IsAvailable { get; set; }

    public virtual ICollection<Booking> Bookings { get; set; } = new List<Booking>();

    public virtual User User { get; set; } = null!;
}
