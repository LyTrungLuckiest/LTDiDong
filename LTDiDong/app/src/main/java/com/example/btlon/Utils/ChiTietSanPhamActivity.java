package com.example.btlon.Utils;

import android.os.Bundle;
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
import com.example.btlon.Data.Product;
import com.example.btlon.R;

import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    TextView tensp, giaSp, mota;
    Button btnThem;
    ImageView imgHinhanh;
    Spinner spinner;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        ActionToolBar();
        initData();
    }


    private void initData() {
        // Lấy dữ liệu từ Intent
        Product sanphammoi = (Product) getIntent().getSerializableExtra("chi tiết");

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