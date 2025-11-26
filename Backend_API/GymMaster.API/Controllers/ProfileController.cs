using GymMaster.API.DTOs; // <--- DÒNG NÀY RẤT QUAN TRỌNG
using GymMaster.API.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Security.Claims;
using System;

namespace GymMaster.API.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class ProfileController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public ProfileController(GymMasterDbContext context)
        {
            _context = context;
        }

        private int GetUserIdFromToken()
        {
            var userIdClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (int.TryParse(userIdClaim, out int userId))
            {
                return userId;
            }
            throw new UnauthorizedAccessException("User ID not found in token.");
        }

        // GET: api/Profile
        [HttpGet]
        public async Task<IActionResult> GetProfile()
        {
            try
            {
                var userId = GetUserIdFromToken();
                var user = await _context.Users
                    .Include(u => u.Role)
                    .FirstOrDefaultAsync(u => u.Id == userId);

                if (user == null) return NotFound("User not found.");

                var profile = new ProfileDTO
                {
                    Id = user.Id,
                    FullName = user.FullName,
                    Email = user.Email,
                    PhoneNumber = user.PhoneNumber,
                    RoleName = user.Role.RoleName
                };

                return Ok(profile);
            }
            catch (UnauthorizedAccessException ex)
            {
                return Unauthorized(new { message = ex.Message });
            }
        }

        // PUT: api/Profile (Cập nhật thông tin)
        [HttpPut]
        public async Task<IActionResult> UpdateProfile([FromBody] UpdateProfileReq request)
        {
            try
            {
                var userId = GetUserIdFromToken();
                var user = await _context.Users.FirstOrDefaultAsync(u => u.Id == userId);

                if (user == null) return NotFound("User not found.");

                // 1. Cập nhật FullName
                user.FullName = request.FullName;

                // 2. Cập nhật PhoneNumber - CHỈ CẬP NHẬT NẾU GIÁ TRỊ GỬI LÊN KHÔNG RỖNG
                if (!string.IsNullOrEmpty(request.PhoneNumber))
                {
                    user.PhoneNumber = request.PhoneNumber;
                }

                await _context.SaveChangesAsync();

                return Ok(new { message = "Cập nhật thông tin thành công!" });
            }
            catch (UnauthorizedAccessException ex)
            {
                return Unauthorized(new { message = ex.Message });
            }
            catch (Exception ex)
            {
                // Nếu có lỗi SQL Server khác, trả về 500
                return StatusCode(500, new { message = "Lỗi server khi cập nhật: " + ex.Message });
            }
        }
    }
}