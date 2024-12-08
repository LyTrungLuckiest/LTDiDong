package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Adapter.CartAdapter;
import com.example.btlon.Data.Cart;
import com.example.btlon.Data.CartProduct;
import com.example.btlon.Data.CartProductTableHelper;
import com.example.btlon.Data.CartTableHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener {
    private TextView giohangtrong, tongtien;
    private RecyclerView recyclerView;
    private Button btnmtienhang, btnXoaAll;
    private Spinner spinnerPaymentMethod;
    private CartAdapter cartAdapter;
    private String userId, selectedPaymentMethod = "Tiền mặt";
    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_cart_fragment, container, false);

        // Initialize PreferenceManager and check login status
        preferenceManager = new PreferenceManager(getContext());
        userId = preferenceManager.getUserId();

        // If user is not logged in, hide cart UI and show message
        if (!preferenceManager.isLoggedIn() || userId == null || userId.isEmpty()) {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập để tiếp tục!", Toast.LENGTH_SHORT).show();
            NestedScrollView scrollView = view.findViewById(R.id.scrollviewCart);
            scrollView.setVisibility(View.GONE);
            return view;
        }

        initializeUI(view);
        setRecyclerViewAdapter(getCart(), getCartProducts());
        setupSpinner();
        setupButtonListeners();
        updateTotalPrice();

        return view;
    }

    private void initializeUI(View view) {
        giohangtrong = view.findViewById(R.id.txtgiohangtrong2);
        tongtien = view.findViewById(R.id.txttongtien);
        recyclerView = view.findViewById(R.id.recyclerviewCart);
        btnmtienhang = view.findViewById(R.id.btntienhang);
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);
        btnXoaAll = view.findViewById(R.id.btnXoaAll);

        ScrollView scrollView = view.findViewById(R.id.scrollviewCart);
        scrollView.setVisibility(preferenceManager.isLoggedIn() ? View.VISIBLE : View.GONE);

        btnXoaAll.setOnClickListener(v -> deleteAllCartProducts());
    }

    private void setRecyclerViewAdapter(List<Cart> carts, Map<Integer, List<CartProduct>> cartProductsMap) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(requireContext(), carts, cartProductsMap, this);
            recyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.updateData(carts, cartProductsMap);
            cartAdapter.notifyDataSetChanged();
        }
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

    private void deleteAllCartProducts() {
        CartTableHelper cartTableHelper = new CartTableHelper(getContext());
        boolean isDeleted = cartTableHelper.deleteCartProductsByCartId(Integer.parseInt(userId));

        if (isDeleted) {
            Log.d("CartFragment", "All cart products deleted successfully.");
            updateUIAfterDeletion();
        } else {
            Log.e("CartFragment", "Failed to delete cart products.");
        }
    }

    private void updateUIAfterDeletion() {
        List<Cart> updatedCarts = getCart();
        Map<Integer, List<CartProduct>> updatedCartProductsMap = getCartProducts();
        cartAdapter.updateData(updatedCarts, updatedCartProductsMap);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        Map<Integer, List<CartProduct>> cartProductsMap = getCartProducts();
        List<CartProduct> cartProductList = cartProductsMap.get(Integer.parseInt(userId));

        if (cartProductList != null) {
            for (CartProduct product : cartProductList) {
                total += product.getTotalPrice();  // Assuming getTotalPrice is a valid method
            }
        }

        tongtien.setText(String.format("%s VND", total));
    }

    private List<Cart> getCart() {
        if (userId == null || userId.isEmpty()) return new ArrayList<>();

        CartTableHelper cartTableHelper = new CartTableHelper(getContext());
        List<Cart> carts = cartTableHelper.getCartsByUserId(Integer.parseInt(userId));
        if (carts.isEmpty()) {
            Cart cart = new Cart(Integer.parseInt(userId), null, new ArrayList<>());
            cartTableHelper.addCart(cart, Integer.parseInt(userId));
            carts.add(cart);
        }
        return carts;
    }

    private Map<Integer, List<CartProduct>> getCartProducts() {
        Map<Integer, List<CartProduct>> map = new HashMap<>();
        CartProductTableHelper cartProductTableHelper = new CartProductTableHelper(getContext());
        List<CartProduct> cartProductList = cartProductTableHelper.getCartProductsByCartId(Integer.parseInt(userId));
        if (cartProductList != null && !cartProductList.isEmpty()) {
            map.put(Integer.parseInt(userId), cartProductList);
        }
        return map;
    }

    // CartUpdateListener method to update total price and UI
    @Override
    public void onCartUpdated() {
        List<Cart> updatedCarts = getCart();
        Map<Integer, List<CartProduct>> updatedCartProductsMap = getCartProducts();
        cartAdapter.updateData(updatedCarts, updatedCartProductsMap);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }
}
