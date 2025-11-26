using GymMaster.API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;

namespace GymMaster.API.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class WorkoutsController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public WorkoutsController(GymMasterDbContext context)
        {
            _context = context;
        }

        private int GetUserIdFromToken()
        {
            var userIdClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (int.TryParse(userIdClaim, out int userId)) return userId;
            throw new UnauthorizedAccessException("User ID not found.");
        }

        // GET: api/Workouts (Danh sách kế hoạch)
        [HttpGet]
        public async Task<IActionResult> GetWorkouts()
        {
            var userId = GetUserIdFromToken();

            // ✅ SỬA LỖI: Thêm .Include(w => w.WorkoutDays) để EF Core tính Count() chính xác
            var workouts = await _context.Workouts
                .Include(w => w.WorkoutDays)
                .Where(w => !w.IsCustom || w.UserId == userId)
                .Select(w => new
                {
                    w.Id,
                    w.Name,
                    w.Description,
                    w.IsCustom,
                    TotalDays = w.WorkoutDays.Count(), // Lệnh này giờ đã hoạt động đúng
                    w.UserId
                })
                .ToListAsync();

            return Ok(workouts);
        }

        // GET: api/Workouts/5/Details (Chi tiết kế hoạch)
        [HttpGet("{id}/Details")]
        public async Task<IActionResult> GetWorkoutDetails(int id)
        {
            var workout = await _context.Workouts
                .Include(w => w.WorkoutDays)
                    .ThenInclude(wd => wd.WorkoutExercises)
                        .ThenInclude(we => we.Exercise) // Include thông tin Bài tập gốc
                .FirstOrDefaultAsync(w => w.Id == id);

            if (workout == null) return NotFound(new { message = "Kế hoạch tập luyện không tồn tại." });

            var details = new
            {
                workout.Id,
                workout.Name,
                workout.Description,
                Days = workout.WorkoutDays.OrderBy(d => d.DayOrder).Select(d => new
                {
                    d.DayOrder,
                    d.Focus,
                    Exercises = d.WorkoutExercises.OrderBy(e => e.Order).Select(e => new
                    {
                        e.Id,
                        e.ExerciseId,
                        e.SetCount,
                        e.RepsPerSet,
                        e.Order,
                        ExerciseName = e.Exercise.Name,
                        ExerciseVideoUrl = e.Exercise.VideoUrl
                    })
                })
            };

            return Ok(details);
        }
    }
}