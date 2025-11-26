using System.Collections.Generic;

namespace GymMaster.API.Models
{
    public partial class Workout
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string Name { get; set; } = null!;
        public string Description { get; set; }
        public bool IsCustom { get; set; } // Kế hoạch do người dùng tự tạo?

        public virtual User User { get; set; } = null!;
        public virtual ICollection<WorkoutDay> WorkoutDays { get; set; } = new List<WorkoutDay>();
    }
}