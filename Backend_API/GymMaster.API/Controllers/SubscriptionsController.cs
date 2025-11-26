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
    public class SubscriptionsController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public SubscriptionsController(GymMasterDbContext context)
        {
            _context = context;
        }

        // Hàm lấy ID người dùng từ Token
        private int GetUserId()
        {
            var idClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            return int.Parse(idClaim);
        }

        // API: Mua gói tập
        [HttpPost]
        public async Task<IActionResult> Subscribe([FromBody] int packageId)
        {
            var userId = GetUserId();

            // 1. Kiểm tra gói tập có tồn tại không
            var package = await _context.GymPackages.FindAsync(packageId);
            if (package == null) return NotFound("Gói tập không tồn tại");

            // 2. Kiểm tra xem user này đang có gói nào còn hạn không (Logic nâng cao)
            var activeSub = await _context.Subscriptions
                .Where(s => s.UserId == userId && s.EndDate > DateTime.Now && s.Status == "Active")
                .FirstOrDefaultAsync();

            if (activeSub != null)
            {
                return BadRequest("Bạn vẫn còn một gói tập đang hoạt động! Không thể mua thêm.");
            }

            // 3. Tạo Subscription mới
            var sub = new Subscription
            {
                UserId = userId,
                PackageId = packageId,
                StartDate = DateTime.Now,
                // LOGIC QUAN TRỌNG: Tính ngày hết hạn dựa trên thời hạn gói
                EndDate = DateTime.Now.AddDays(package.DurationInDays),
                Status = "Active"
            };

            _context.Subscriptions.Add(sub);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Đăng ký gói tập thành công!", expireDate = sub.EndDate });
        }
    }
}