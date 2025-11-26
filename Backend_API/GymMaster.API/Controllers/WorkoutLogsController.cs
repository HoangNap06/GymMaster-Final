using GymMaster.API.DTOs;
using GymMaster.API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;

namespace GymMaster.API.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class WorkoutLogsController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public WorkoutLogsController(GymMasterDbContext context)
        {
            _context = context;
        }

        private int GetUserIdFromToken()
        {
            var userIdClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (int.TryParse(userIdClaim, out int userId)) return userId;
            throw new UnauthorizedAccessException("User ID not found.");
        }

        // GET: api/WorkoutLogs
        [HttpGet]
        public async Task<IActionResult> GetLogs()
        {
            var userId = GetUserIdFromToken();
            var logs = await _context.WorkoutLogs
                .Where(w => w.UserId == userId)
                .OrderByDescending(w => w.DateLogged)
                .Select(w => new WorkoutLogDTO
                {
                    Id = w.Id,
                    DateLogged = w.DateLogged,
                    DurationMinutes = w.DurationMinutes,
                    CaloriesBurned = w.CaloriesBurned,
                    Notes = w.Notes
                })
                .ToListAsync();

            return Ok(logs);
        }

        // POST: api/WorkoutLogs
        [HttpPost]
        public async Task<IActionResult> AddLog([FromBody] AddWorkoutLogReq req)
        {
            var userId = GetUserIdFromToken();
            var log = new WorkoutLog
            {
                UserId = userId,
                DateLogged = DateTime.Now,
                DurationMinutes = req.DurationMinutes,
                CaloriesBurned = req.CaloriesBurned,
                Notes = req.Notes
            };

            _context.WorkoutLogs.Add(log);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Đã lưu nhật ký tập luyện!" });
        }
    }
}