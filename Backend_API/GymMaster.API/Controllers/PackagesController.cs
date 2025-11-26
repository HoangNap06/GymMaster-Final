using GymMaster.API.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Authorization; // Thêm dòng này
namespace GymMaster.API.Controllers
{
    [Authorize] // <--- Dòng này BẮT BUỘC có Token mới được gọi
    [Route("api/[controller]")]
    [ApiController]
    public class PackagesController : ControllerBase
    {
        private readonly GymMasterDbContext _context;

        public PackagesController(GymMasterDbContext context)
        {
            _context = context;
        }

        // API: GET api/Packages
        [HttpGet]
        public async Task<IActionResult> GetAllPackages()
        {
            // Lấy tất cả gói tập đang hoạt động (IsActive = true)
            var packages = await _context.GymPackages
                                         .Where(p => p.IsActive == true) // Chỉ lấy gói đang bán
                                         .ToListAsync();

            return Ok(packages);
        }
    }
}