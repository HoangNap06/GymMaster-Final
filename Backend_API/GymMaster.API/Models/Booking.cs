using System;
using System.Collections.Generic;

namespace GymMaster.API.Models;

public partial class Booking
{
    public int Id { get; set; }

    public int MemberId { get; set; }

    public int TrainerId { get; set; }

    public DateTime AppointmentTime { get; set; }

    public string? Status { get; set; }

    public DateTime? BookingDate { get; set; }

    public virtual User Member { get; set; } = null!;

    public virtual Trainer Trainer { get; set; } = null!;
}
