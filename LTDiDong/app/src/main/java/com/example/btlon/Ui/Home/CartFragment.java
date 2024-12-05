package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Adapter.CartAdapter;
import com.example.btlon.Data.Cart;
import com.example.btlon.Data.CartTableHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private TextView giohangtrong, tongtien;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button btnmtienhang, btnXoaAll;
    private Spinner spinnerPaymentMethod;
    private String selectedPaymentMethod = "Tiền mặt";
    private CartAdapter cartAdapter;
    private List<Cart> cartProducts = new ArrayList<>();

    private CartTableHelper cartTableHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_cart_fragment, container, false);

        // Initialize UI components
        giohangtrong = view.findViewById(R.id.txtgiohangtrong2);
        tongtien = view.findViewById(R.id.txttongtien);
        recyclerView = view.findViewById(R.id.recyclerviewgiohang);
        btnmtienhang = view.findViewById(R.id.btntienhang);
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);
        btnXoaAll = view.findViewById(R.id.btnXoaAll);

        cartTableHelper = new CartTableHelper(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(requireContext(), cartProducts, cartTableHelper);
        recyclerView.setAdapter(cartAdapter);

        setupSpinner();
        setupButtonListeners();
        loadCartProducts();
        btnXoaAll.setOnClickListener(v -> {
            PreferenceManager preferenceManager = new PreferenceManager(requireContext());
            String id = preferenceManager.getUserId();
            cartTableHelper.clearCart(Integer.parseInt(id));
            cartProducts.clear();
            cartAdapter.notifyDataSetChanged();
            loadCartProducts();
        });
        return view;
    }


    private void loadCartProducts() {
        PreferenceManager preferenceManager = new PreferenceManager(requireContext());
        String id = preferenceManager.getUserId();

        cartProducts = cartTableHelper.getCartItems(Integer.parseInt(id));

        if (cartProducts != null && !cartProducts.isEmpty()) {
            cartAdapter = new CartAdapter(requireContext(), cartProducts, cartTableHelper);
            recyclerView.setAdapter(cartAdapter);
            giohangtrong.setVisibility(View.GONE); // Ẩn thông báo giỏ hàng trống
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();


        } else {
            giohangtrong.setText("Giỏ hàng trống");
            tongtien.setText("0 VND");
        }
    }

    private void updateTotalPrice() {
        double total = 0;
        for (Cart product : cartProducts) {
            total += product.getPrice() * product.getQuantity();
        }
        tongtien.setText(String.format("%s VND", total)); // Cập nhật tổng tiền
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
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupButtonListeners() {
        btnmtienhang.setOnClickListener(v -> handleCheckout());
    }

    private void handleCheckout() {
        Toast.makeText(requireContext(), "Đang xử lý thanh toán: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();
    }
}
