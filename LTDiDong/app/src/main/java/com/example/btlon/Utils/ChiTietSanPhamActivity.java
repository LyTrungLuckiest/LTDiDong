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
import com.example.btlon.Data.Products;
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
    Products sanPhamMoi;
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
            Products product = new Products(); // Dữ liệu sản phẩm
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
        Products sanphammoi = (Products) getIntent().getSerializableExtra("chi tiết");


            sanPhamMoi = sanphammoi;
            if (sanphammoi != null) {
                tensp.setText(sanphammoi.getName());
                mota.setText(sanphammoi.getDescription());
                Glide.with(getApplicationContext()).load(sanphammoi.getImageUrl()).into(imgHinhanh);

                DecimalFormat decimal = new DecimalFormat("###,###,###");
                giaSp.setText("Giá: " + decimal.format(Double.parseDouble(sanphammoi.getPrice())) + "Đ");

                // Thiết lập Spinner số lượng
                Integer[] so = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
                ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, so);
                spinner.setAdapter(adapter);
            } else {
                // Xử lý nếu không có dữ liệu
                tensp.setText("Không có dữ liệu sản phẩm");
                mota.setText("");
                giaSp.setText("");
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