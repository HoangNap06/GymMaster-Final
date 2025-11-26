using GymMaster.API.DTOs;
using GymMaster.API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System.Threading.Tasks;

namespace GymMaster.API.Controllers
{
    [Authorize] // Yêu cầu đăng nhập mới được gọi API này
    [Route("api/[controller]")]
    [ApiController]
    public class MuscleGroupsController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public MuscleGroupsController(GymMasterDbContext context)
        {
            _context = context;
        }

        // GET: api/MuscleGroups
        // Chức năng: Lấy danh sách tất cả các nhóm cơ
        [HttpGet]
        public async Task<IActionResult> GetMuscleGroups()
        {
            // Truy vấn Database lấy danh sách nhóm cơ và chuyển đổi sang DTO
            var groups = await _context.MuscleGroups
                .Select(m => new MuscleGroupDTO
                {
                    Id = m.Id,
                    Name = m.Name,
                    ImageUrl = m.ImageUrl
                })
                .ToListAsync();

            return Ok(groups);
        }

        // GET: api/MuscleGroups/{id}
        // Chức năng: Lấy thông tin chi tiết một nhóm cơ theo ID
        [HttpGet("{id}")]
        public async Task<IActionResult> GetMuscleGroupById(int id)
        {
            var muscleGroup = await _context.MuscleGroups.FindAsync(id);

            if (muscleGroup == null)
            {
                return NotFound(new { message = "Không tìm thấy nhóm cơ này." });
            }

            var groupDto = new MuscleGroupDTO
            {
                Id = muscleGroup.Id,
                Name = muscleGroup.Name,
                ImageUrl = muscleGroup.ImageUrl
            };

            return Ok(groupDto);
        }
    }
}