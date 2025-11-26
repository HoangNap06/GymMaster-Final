namespace GymMaster.API.Models
{
    public partial class WorkoutExercise
    {
        public int Id { get; set; }
        public int WorkoutDayId { get; set; }
        public int ExerciseId { get; set; } // ID bài tập từ bảng Exercises
        public int SetCount { get; set; } // Số hiệp
        public int RepsPerSet { get; set; } // Số lần lặp
        public int Order { get; set; } // Thứ tự bài tập trong ngày

        public virtual WorkoutDay WorkoutDay { get; set; } = null!;
        public virtual Exercise Exercise { get; set; } = null!;
    }
}