using System.Collections.Generic;

namespace GymMaster.API.Models
{
    public partial class WorkoutDay
    {
        public int Id { get; set; }
        public int WorkoutId { get; set; }
        public int DayOrder { get; set; } // Thứ tự ngày trong tuần (1=Thứ 2, 7=Chủ Nhật)
        public string Focus { get; set; } = null!; // Tập trung vào nhóm cơ nào (VD: Chest & Triceps)

        public virtual Workout Workout { get; set; } = null!;
        public virtual ICollection<WorkoutExercise> WorkoutExercises { get; set; } = new List<WorkoutExercise>();
    }
}