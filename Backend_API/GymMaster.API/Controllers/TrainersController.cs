using GymMaster.API.DTOs;
using GymMaster.API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace GymMaster.API.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class TrainersController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public TrainersController(GymMasterDbContext context)
        {
            _context = context;
        }

        // GET: api/Trainers
        [HttpGet]
        public async Task<IActionResult> GetAllTrainers()
        {
            var trainers = await _context.Users
                .Where(u => u.Role.RoleName == "Trainer")
                .Join(_context.Trainers,
                      user => user.Id,
                      trainer => trainer.UserId,
                      (user, trainer) => new TrainerDTO
                      {
                          // --- QUAN TRỌNG: Phải lấy ID của bảng Trainer ---
                          Id = trainer.Id, // Lấy ID từ bảng Trainer

                          FullName = user.FullName,
                          Email = user.Email,
                          AvatarUrl = user.AvatarUrl ?? "",
                          Specialization = trainer.Specialization,
                          Bio = trainer.Bio,
                          HourlyRate = trainer.HourlyRate ?? 0
                      })
                .ToListAsync();

            return Ok(trainers);
        }
    }
}