using GymMaster.API.Models;
using GymMaster.API.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Http.Features;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using Microsoft.OpenApi.Models;
using System.Text;

var builder = WebApplication.CreateBuilder(args);

// 1. Lắng nghe trên HTTP/5114 (Đồng bộ với App Android)
builder.WebHost.UseUrls("http://localhost:5114", "http://0.0.0.0:5114");

// ==================================================================
// 2. CẤU HÌNH SERVICES & KHAI BÁO 
// ==================================================================
// Tăng giới hạn Multipart Body (cho Video)
builder.Services.Configure<FormOptions>(options =>
{
    options.MultipartBodyLengthLimit = 524_288_000; // 500 MB
});

builder.WebHost.ConfigureKestrel(serverOptions =>
{
    serverOptions.Limits.MaxRequestBodySize = 524_288_000; // 500 MB
});

// Thêm Controllers và giữ nguyên tên biến (PascalCase)
builder.Services.AddControllers()
    .AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.PropertyNamingPolicy = null;
    });

// 3. KẾT NỐI DATABASE & SERVICES
builder.Services.AddScoped<JwtService>();

builder.Services.AddDbContext<GymMasterDbContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("GymMasterCS")));

// 4. CẤU HÌNH CORS
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll",
        builder => builder.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader());
});

// 5. CẤU HÌNH JWT AUTHENTICATION
var securityKey = builder.Configuration["JwtSettings:SecurityKey"];
var issuer = builder.Configuration["JwtSettings:Issuer"];
var audience = builder.Configuration["JwtSettings:Audience"];
var keyBytes = Encoding.ASCII.GetBytes(securityKey);

builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
})
.AddJwtBearer(options =>
{
    options.RequireHttpsMetadata = false; // Tắt kiểm tra HTTPS
    options.SaveToken = true;
    options.TokenValidationParameters = new TokenValidationParameters
    {
        ValidateIssuerSigningKey = true,
        IssuerSigningKey = new SymmetricSecurityKey(keyBytes),
        ValidateIssuer = true,
        ValidIssuer = issuer,
        ValidateAudience = true,
        ValidAudience = audience,
        ValidateLifetime = true,
        ClockSkew = TimeSpan.Zero
    };
});

// 6. CẤU HÌNH SWAGGER
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo { Title = "GymMaster API", Version = "v1" });
    c.AddSecurityDefinition("Bearer", new OpenApiSecurityScheme
    {
        Description = "Nhập token theo định dạng: Bearer {token}",
        Name = "Authorization",
        In = ParameterLocation.Header,
        Type = SecuritySchemeType.ApiKey,
        Scheme = "Bearer"
    });
    c.AddSecurityRequirement(new OpenApiSecurityRequirement
    {
        {
            new OpenApiSecurityScheme
            {
                Reference = new OpenApiReference
                {
                    Type = ReferenceType.SecurityScheme,
                    Id = "Bearer"
                }
            },
            new string[] {}
        }
    });
});

var app = builder.Build();

// ==========================================
// 7. CẤU HÌNH PIPELINE (MIDDLEWARE)
// ==========================================
// Tự động tạo thư mục lưu Video
var videoPath = Path.Combine(app.Environment.ContentRootPath, "wwwroot", "videos"); // FIX: Dùng wwwroot/videos
if (!Directory.Exists(videoPath))
{
    Directory.CreateDirectory(videoPath);
}

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseStaticFiles(); // Cho phép truy cập file tĩnh

// Đã xóa UseHttpsRedirection()
// app.UseHttpsRedirection(); 

app.UseCors("AllowAll");

app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();