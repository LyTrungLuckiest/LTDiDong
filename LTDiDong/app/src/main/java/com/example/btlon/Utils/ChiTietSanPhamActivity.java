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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.btlon.Data.CartTableHelper;
import com.example.btlon.Data.Product;
import com.example.btlon.R;
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
    ImageView Tru, Cong;
    TextView Soluong;

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

        // Call to initialize views
        initView();
        ActionToolBar();
        initData();

        // Button click to add to cart
        Button addToCartButton = findViewById(R.id.btnthemvaogiohang);
        addToCartButton.setOnClickListener(v -> {
            if (sanPhamMoi != null) {
                // Get selected quantity from spinner
                int quantity= Integer.parseInt(Soluong.getText().toString());
                PreferenceManager preferenceManager = new PreferenceManager(ChiTietSanPhamActivity.this);
                String userId = preferenceManager.getUserId();
                int productId = sanPhamMoi.getId();  // Corrected variable name

                // Add product to cart
                CartTableHelper cartTableHelper = new CartTableHelper(ChiTietSanPhamActivity.this);
                boolean success = cartTableHelper.addItemToCart(Integer.parseInt(userId), productId, quantity); // Corrected variable name

                if (success) {
                    Toast.makeText(ChiTietSanPhamActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChiTietSanPhamActivity.this, "Có lỗi xảy ra, không thể thêm sản phẩm", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChiTietSanPhamActivity.this, "Có lỗi xảy ra, không thể thêm sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
        Tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(Soluong.getText().toString());
                if (currentQuantity > 1) {
                    currentQuantity--;
                    Soluong.setText(String.valueOf(currentQuantity));
                }
            }
        });
        Cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(Soluong.getText().toString());
                currentQuantity++;
                Soluong.setText(String.valueOf(currentQuantity));
            }
        });
    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giaSp = findViewById(R.id.txtgia);
        mota = findViewById(R.id.txtmotachitiet);
        btnThem = findViewById(R.id.btnthemvaogiohang);
        imgHinhanh = findViewById(R.id.imageChiTiet);
        toolbar = findViewById(R.id.toolbar);
        Tru = findViewById(R.id.Tru);
        Cong = findViewById(R.id.Cong);
        Soluong = findViewById(R.id.Soluong);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initData() {
        // Get data from Intent
        Product sanphammoi = (Product) getIntent().getSerializableExtra("product");
        sanPhamMoi = sanphammoi;

        if (sanphammoi != null) {
            tensp.setText(sanphammoi.getName() != null ? sanphammoi.getName() : "Tên sản phẩm không có");
            mota.setText(sanphammoi.getDescription() != null ? sanphammoi.getDescription() : "Mô tả không có");

            // Load image
            if (sanphammoi.getImageUrl() != null) {
                Glide.with(getApplicationContext()).load(sanphammoi.getImageUrl()).into(imgHinhanh);
            } else {
                imgHinhanh.setImageResource(R.drawable.error_image); // Fallback image
            }

            // Format price
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

        } else {
            tensp.setText("Không có dữ liệu sản phẩm");
            mota.setText("");
            giaSp.setText("");
            imgHinhanh.setImageResource(R.drawable.error_image); // Fallback image
        }
    }
}
