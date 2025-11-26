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
    public class BookingsController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public BookingsController(GymMasterDbContext context)
        {
            _context = context;
        }

        private int GetUserIdFromToken()
        {
            var userIdClaim = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;
            if (int.TryParse(userIdClaim, out int userId)) return userId;
            throw new UnauthorizedAccessException("User ID not found.");
        }

        // POST: api/Bookings
        [HttpPost]
        public async Task<IActionResult> CreateBooking([FromBody] BookingReq req)
        {
            var memberId = GetUserIdFromToken();

            // 1. Kiểm tra PT tồn tại
            var trainerExists = await _context.Trainers.AnyAsync(t => t.Id == req.TrainerId);
            if (!trainerExists) return NotFound(new { message = "PT không tồn tại." });

            // 2. LOGIC FIX: Kiểm tra xem PT đã bận vào giờ đó chưa?
            // (Bỏ qua các lịch đã bị hủy)
            var isBusy = await _context.Bookings.AnyAsync(b =>
                b.TrainerId == req.TrainerId &&
                b.AppointmentTime == req.AppointmentTime &&
                b.Status != "Cancelled");

            if (isBusy)
            {
                return BadRequest(new { message = "HLV đã có lịch vào khung giờ này. Vui lòng chọn giờ khác." });
            }

            var booking = new Booking
            {
                MemberId = memberId,
                TrainerId = req.TrainerId,
                AppointmentTime = req.AppointmentTime,
                Status = "Pending",
                BookingDate = DateTime.Now
            };

            _context.Bookings.Add(booking);
            await _context.SaveChangesAsync();

            return Ok(new { message = "Đặt lịch thành công!" });
        }
    }
}