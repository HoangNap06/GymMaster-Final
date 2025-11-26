using GymMaster.API.DTOs;
using GymMaster.API.Models;
using GymMaster.API.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System; // Cần thiết cho DateTime.Now

namespace GymMaster.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly GymMasterDbContext _context;
        private readonly JwtService _jwtService;

        public AuthController(GymMasterDbContext context, JwtService jwtService)
        {
            _context = context;
            _jwtService = jwtService;
        }

        // ==========================================
        // 1. API ĐĂNG NHẬP
        // ==========================================
        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginReq request)
        {
            // A. BỎ MÃ HÓA SHA256 - SỬ DỤNG PLAIN TEXT (Không khuyến khích bảo mật)
            string inputPassword = request.Password;

            // B. Tìm user trong database
            var user = await _context.Users
                .Include(u => u.Role) // BẮT BUỘC INCLUDE ROLE ĐỂ DÙNG user.Role.RoleName
                .FirstOrDefaultAsync(u => u.Email == request.Email);

            // C. So sánh mật khẩu trực tiếp (Plain Text)
            if (user == null || user.PasswordHash != inputPassword) // Sửa: So sánh PasswordHash (chứa Plain Text) với inputPassword
            {
                return Unauthorized(new { message = "Email hoặc mật khẩu không đúng!" });
            }

            // D. Logic kiểm tra Role (Member/Admin/Trainer)
            if (user.Role.RoleName == "Trainer")
            {
                return Unauthorized(new { message = "Tài khoản Huấn luyện viên không thể đăng nhập vào ứng dụng này!" });
            }

            // E. Tạo Token
            var roleName = user.Role.RoleName;
            var token = _jwtService.GenerateToken(user, roleName);

            // F. Trả về kết quả
            return Ok(new
            {
                message = "Đăng nhập thành công!",
                userId = user.Id,
                fullName = user.FullName,
                role = roleName,
                token = token
            });
        } // <--- KẾT THÚC HÀM LOGIN Ở ĐÂY

        // ==========================================
        // 2. API ĐĂNG KÝ (Nằm ngang hàng với Login)
        // ==========================================
        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterReq request)
        {
            // 1. Kiểm tra Email trùng
            var emailExists = await _context.Users.AnyAsync(u => u.Email == request.Email);
            if (emailExists)
            {
                return BadRequest(new { message = "Email này đã được sử dụng!" });
            }

            // Tìm Role ID
            var memberRole = await _context.Roles.FirstOrDefaultAsync(r => r.RoleName == "Member");

            if (memberRole == null)
            {
                return StatusCode(500, new { message = "Lỗi Server: Chưa cấu hình Role 'Member' trong Database" });
            }

            // 2. BỎ MÃ HÓA - LƯU PLAIN TEXT VÀO CỘT PASSWORDHASH
            string passwordToSave = request.Password;

            // 3. Tạo User mới
            var newUser = new User
            {
                FullName = request.FullName,
                Email = request.Email,
                PasswordHash = passwordToSave, // LƯU PLAIN TEXT
                PhoneNumber = request.PhoneNumber,
                RoleId = memberRole.Id,
                CreateAt = DateTime.Now,
                AvatarUrl = ""
            };

            _context.Users.Add(newUser);

            try
            {
                await _context.SaveChangesAsync();
                return Ok(new { message = "Đăng ký thành công! Vui lòng đăng nhập." });
            }
            catch (Exception ex)
            {
                // In lỗi ra để biết tại sao (nếu có lỗi khác)
                return StatusCode(500, new { message = "Lỗi lưu Database: " + ex.Message });
            }
        }
    }
}