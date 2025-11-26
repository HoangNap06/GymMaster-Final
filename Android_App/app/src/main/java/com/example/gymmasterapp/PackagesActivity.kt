package com.example.gymmasterapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packages)

        val rvPackages = findViewById<RecyclerView>(R.id.rvPackages)
        rvPackages.layoutManager = LinearLayoutManager(this)

        // Tải danh sách gói tập
        ApiClient.getService(this).getPackages().enqueue(object : Callback<List<GymPackage>> {
            override fun onResponse(call: Call<List<GymPackage>>, response: Response<List<GymPackage>>) {
                if (response.isSuccessful) {
                    val packageList = response.body() ?: emptyList()

                    // Truyền hàm xử lý sự kiện Mua
                    val adapter = PackageAdapter(packageList) { selectedPkg ->
                        showConfirmDialog(selectedPkg)
                    }
                    rvPackages.adapter = adapter
                } else {
                    Toast.makeText(this@PackagesActivity, "Lỗi tải gói tập", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<GymPackage>>, t: Throwable) {
                Toast.makeText(this@PackagesActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Hiện hộp thoại xác nhận trước khi mua (cho chuyên nghiệp)
    private fun showConfirmDialog(pkg: GymPackage) {
        AlertDialog.Builder(this)
            .setTitle("Xác nhận đăng ký")
            .setMessage("Bạn có chắc muốn đăng ký gói '${pkg.packageName}' với giá ${pkg.price} không?")
            .setPositiveButton("Đồng ý") { _, _ ->
                buyPackage(pkg.id)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    // Gọi API mua gói
    private fun buyPackage(packageId: Int) {
        ApiClient.getService(this).subscribe(packageId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PackagesActivity, "Đăng ký thành công! Thẻ tập đã kích hoạt.", Toast.LENGTH_LONG).show()
                    finish() // Đóng màn hình, quay về Home
                } else {
                    // Có thể lỗi do đã có gói tập rồi (Logic ở Backend trả về 400)
                    Toast.makeText(this@PackagesActivity, "Đăng ký thất bại (Có thể bạn đang còn hạn thẻ)", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@PackagesActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}