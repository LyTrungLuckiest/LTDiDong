package com.example.btlon.Utils;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.GioHang;
import com.example.btlon.Data.Product;
import com.example.btlon.R;
import com.example.btlon.Ui.Home.CartFragment;
import com.example.btlon.Ui.Home.HomeActivity;

import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    TextView tensp, giaSp, mota;
    Button btnThem;
    ImageView imgHinhanh;
    Spinner spinner;
    Toolbar toolbar;
    Product sanPhamMoi;
    Notification badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        // Tham chiếu đến nút giỏ hàng
        View frameGioHang = findViewById(R.id.framegiohang);

        // Đặt OnClickListener
        frameGioHang.setOnClickListener(v -> {
            // Chuyển sang Tab Giỏ hàng
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("tabIndex", 1); // 1 là chỉ số tab giỏ hàng
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
        ActionToolBar();
        initData();
        initControl();
        // Xử lý sự kiện click vào sản phẩm
        findViewById(R.id.btnthemvaogiohang).setOnClickListener(v -> {
            Product product = new Product();
            Intent intent = new Intent(ChiTietSanPhamActivity.this, ChiTietSanPhamActivity.class);
            intent.putExtra("chi tiết", product);
            startActivity(intent);
        });
    }

    private void initControl() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
            }
        });

    }

    private void themGioHang() {


    }


    private void initData() {
        // Lấy dữ liệu từ Intent
        Product sanphammoi = (Product) getIntent().getSerializableExtra("product");

        sanPhamMoi = sanphammoi; // Gán sản phẩm mới

        if (sanphammoi != null) {
            tensp.setText(sanphammoi.getName() != null ? sanphammoi.getName() : "Tên sản phẩm không có");
            mota.setText(sanphammoi.getDescription() != null ? sanphammoi.getDescription() : "Mô tả không có");

            // Tải hình ảnh (kiểm tra null)
            if (sanphammoi.getImageUrl() != null) {
                Glide.with(getApplicationContext()).load(sanphammoi.getImageUrl()).into(imgHinhanh);
            } else {
                imgHinhanh.setImageResource(R.drawable.error_image); // Hình ảnh thay thế
            }

            // Kiểm tra và định dạng giá
            if (sanphammoi.getPrice() != null && !sanphammoi.getPrice().isEmpty()) {
                try {
                    DecimalFormat decimal = new DecimalFormat("###,###,###");
                    double price = Double.parseDouble(sanphammoi.getPrice().trim());
                    giaSp.setText("Giá: " + decimal.format(price) + "Đ");
                } catch (NumberFormatException e) {
                    giaSp.setText("Giá: Không hợp lệ");
                }
            } else {
                giaSp.setText("Giá: Không xác định");
            }

            // Thiết lập Spinner số lượng
            Integer[] so = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, so);
            spinner.setAdapter(adapter);

        } else {
            // Nếu không có sản phẩm
            tensp.setText("Không có dữ liệu sản phẩm");
            mota.setText("");
            giaSp.setText("");
            imgHinhanh.setImageResource(R.drawable.error_image); // Hình ảnh thay thế
        }
    }


    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giaSp = findViewById(R.id.txtgia);
        mota = findViewById(R.id.txtmotachitiet);
        btnThem = findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spinner);
        imgHinhanh = findViewById(R.id.imageChiTiet);
        toolbar = findViewById(R.id.toolbar);

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}