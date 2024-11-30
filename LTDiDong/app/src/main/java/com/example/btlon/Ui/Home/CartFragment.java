package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Adapter.CartAdapter;
import com.example.btlon.Data.GioHang;
import com.example.btlon.R;
import com.example.btlon.Utils.Utils;

public class CartFragment extends Fragment {
    private TextView giohangtrong, tongtien;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnmtienhang;
    private Spinner spinnerPaymentMethod;
    private String selectedPaymentMethod = "Tiền mặt";
    private CartAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_cart_fragment, container, false);

        // Initialize UI components
        giohangtrong = view.findViewById(R.id.txtgiohangtrong2);
        tongtien = view.findViewById(R.id.txttongtien);
        toolbar = view.findViewById(R.id.toolbar);
        recyclerView = view.findViewById(R.id.recyclerviewgiohang);
        btnmtienhang = view.findViewById(R.id.btntienhang);
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);

        setupToolbar();
        setupRecyclerView();
        setupSpinner();
        setupButtonListeners();

        return view;
    }

    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    private void setupRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        updateCartView();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Tiền mặt", "MoMo", "ZaloPay", "Ngân hàng"});
        spinnerPaymentMethod.setAdapter(adapter);
        spinnerPaymentMethod.setSelection(0);

        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupButtonListeners() {
        btnmtienhang.setOnClickListener(v -> handleCheckout());
    }

    private void handleCheckout() {
        switch (selectedPaymentMethod) {
            case "Tiền mặt":
                processCashPayment();
                break;
            case "MoMo":
                processMoMoPayment();
                break;
            case "ZaloPay":
                processZaloPayPayment();
                break;
            case "Ngân hàng":
                processBankPayment();
                break;
            default:
                Toast.makeText(requireContext(), "Vui lòng chọn phương thức thanh toán hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void processCashPayment() {
        Toast.makeText(requireContext(), "Thanh toán bằng tiền mặt thành công!", Toast.LENGTH_SHORT).show();
    }

    private void processMoMoPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến MoMo...", Toast.LENGTH_SHORT).show();
    }

    private void processZaloPayPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến ZaloPay...", Toast.LENGTH_SHORT).show();
    }

    private void processBankPayment() {
        Toast.makeText(requireContext(), "Đang chuyển đến thanh toán ngân hàng...", Toast.LENGTH_SHORT).show();
    }

    private void updateCartView() {
        // Cập nhật giao diện giỏ hàng, ví dụ kiểm tra giỏ hàng có sản phẩm hay không
        if (Utils.manggiohang.isEmpty()) {
            giohangtrong.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tongtien.setText("0 Đ");
        } else {
            giohangtrong.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            // Cập nhật RecyclerView
            adapter.notifyDataSetChanged(); // Làm mới dữ liệu giỏ hàng
            updateTotalPrice(); // Cập nhật tổng tiền
        }

        updateTotalPrice(); // Tính lại tổng tiền
    }
    private void onCartUpdated() {
        updateTotalPrice(); // Tính lại tổng tiền
    }
    private void updateTotalPrice() {
        if (Utils.manggiohang.isEmpty()) {
            tongtien.setText("Giỏ hàng của bạn đang trống");
        } else {
            long total = 0;
            for (GioHang gioHang : Utils.manggiohang) {
                total += gioHang.getGiaSp() * gioHang.getSoLuong();
            }
            tongtien.setText(String.format("%,d Đ", total)); // Hiển thị tổng tiền
        }
    }

}
