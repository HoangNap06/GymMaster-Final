using GymMaster.API.DTOs;
using GymMaster.API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;
using System.Collections.Generic;

namespace GymMaster.API.Controllers
{
    [Authorize] // Bắt buộc đăng nhập mới xem được danh sách bài tập
    [Route("api/[controller]")]
    [ApiController]
    public class ExercisesController : ControllerBase
    {
        // Khắc phục cảnh báo Nullability
        private readonly GymMasterDbContext _context = null!;

        public ExercisesController(GymMasterDbContext context)
        {
            _context = context;
        }

        // 1. GET: api/Exercises/ByMuscleGroup/{muscleGroupId}
        [HttpGet("ByMuscleGroup/{muscleGroupId}")]
        public async Task<IActionResult> GetExercisesByMuscleGroup(int muscleGroupId)
        {
            var exercises = await _context.Exercises
                .Where(e => e.MuscleGroupId == muscleGroupId)
                .Select(e => new ExerciseDTO
                {
                    Id = e.Id,
                    Name = e.Name,
                    Description = e.Description,
                    VideoUrl = e.VideoUrl,
                    ImageUrl = e.ImageUrl,
                    MuscleGroupId = e.MuscleGroupId
                })
                .ToListAsync();

            return Ok(exercises);
        }

        // 2. GET: api/Exercises/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetExerciseDetail(int id)
        {
            var exercise = await _context.Exercises
                .Include(e => e.ExerciseSteps)
                .Include(e => e.MuscleGroup)
                .FirstOrDefaultAsync(e => e.Id == id);

            if (exercise == null) return NotFound(new { message = "Bài tập không tồn tại." });

            // Chuyển đổi sang DTO chi tiết
            var detail = new ExerciseDetailsResponse
            {
                // Ánh xạ các trường kế thừa từ ExerciseDTO
                Id = exercise.Id,
                Name = exercise.Name,
                Description = exercise.Description,
                VideoUrl = exercise.VideoUrl,
                ImageUrl = exercise.ImageUrl,

                // Ánh xạ các trường riêng của ExerciseDetailsResponse
                MuscleGroup = exercise.MuscleGroup?.Name ?? "Không xác định",

                // FIX LOGIC ÁNH XẠ: Dùng StepNumber, Title, Detail từ Model
                Steps = exercise.ExerciseSteps
                    .OrderBy(s => s.StepOrder)
                    .Select(s => new ExerciseStepDTO
                    {
                        StepNumber = s.StepNumber, // Lấy StepNumber từ Model
                        Title = s.Title,          // Lấy Title từ Model
                        Detail = s.Detail,        // Lấy Detail từ Model
                        ImageUrl = s.ImageUrl
                    })
                    .ToList()
            };

            return Ok(detail);
        }
    }
}