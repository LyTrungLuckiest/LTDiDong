package com.example.btlon.Ui.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Adapter.CartAdapter;
import com.example.btlon.Data.Cart;
import com.example.btlon.Data.CartProduct;
import com.example.btlon.Data.CartProductTableHelper;
import com.example.btlon.Data.CartTableHelper;
import com.example.btlon.Models.CreateOrder;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartFragment extends Fragment implements CartAdapter.CartUpdateListener {
    private TextView gioHangTrong, tongTien;
    private RecyclerView recyclerView;
    private Button btnThanhToan, btnXoaAll;
    private Spinner spinnerPaymentMethod;
    private CartAdapter cartAdapter;
    private String userId, selectedPaymentMethod = "Tiền mặt";
    private PreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ZaloPay SDK Init
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_cart_fragment, container, false);

        preferenceManager = new PreferenceManager(requireContext());
        userId = preferenceManager.getUserId();

        if (!preferenceManager.isLoggedIn() || TextUtils.isEmpty(userId)) {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập để tiếp tục!", Toast.LENGTH_SHORT).show();
            ScrollView scrollView = view.findViewById(R.id.scrollviewCart);
            scrollView.setVisibility(View.GONE);
            return view;
        }

        initializeUI(view);
        setupRecyclerView();
        setupSpinner();
        updateTotalPrice();
        return view;
    }

    private void initializeUI(View view) {
        gioHangTrong = view.findViewById(R.id.txtgiohangtrong2);
        tongTien = view.findViewById(R.id.txttongtien);
        recyclerView = view.findViewById(R.id.recyclerviewCart);
        btnThanhToan = view.findViewById(R.id.btntienhang);
        btnXoaAll = view.findViewById(R.id.btnXoaAll);
        spinnerPaymentMethod = view.findViewById(R.id.spinnerPaymentMethod);

        btnThanhToan.setOnClickListener(v -> handlePayment());
        btnXoaAll.setOnClickListener(v -> deleteAllCartProducts());
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(requireContext(), getCart(), getCartProducts(), this);
        recyclerView.setAdapter(cartAdapter);
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

    private void handlePayment() {
        double total = getTotalPrice();
        if (total <= 0) {
            Toast.makeText(requireContext(), "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (selectedPaymentMethod) {
            case "Tiền mặt":
                navigateToResult("Thanh toán tiền mặt thành công!");
                break;
            case "ZaloPay":
                handleZaloPayPayment(total);
                break;
            default:
                Toast.makeText(requireContext(), "Phương thức chưa hỗ trợ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleZaloPayPayment(double total) {
        try {
            CreateOrder orderApi = new CreateOrder();
            JSONObject data = orderApi.createOrder(String.valueOf((int) total));
            if ("1".equals(data.getString("return_code"))) {
                ZaloPaySDK.getInstance().payOrder(requireActivity(), data.getString("zp_trans_token"), "yourapp://callback", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String zpTransId, String message) {
                        navigateToResult("Thanh toán thành công!");
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String message) {
                        navigateToResult("Thanh toán đã bị hủy!");
                    }

                    @Override
                    public void onPaymentError(vn.zalopay.sdk.ZaloPayError zaloPayError, String zpTransToken, String message) {
                        Toast.makeText(requireContext(), "Thanh toán thất bại: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("CartFragment", "Lỗi ZaloPay", e);
        }
    }

    private void navigateToResult(String result) {
        Intent intent = new Intent(requireActivity(), PaymenActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    private void deleteAllCartProducts() {
        CartTableHelper cartTableHelper = new CartTableHelper(requireContext());
        if (cartTableHelper.deleteCartProductsByCartId(Integer.parseInt(userId))) {
            Toast.makeText(requireContext(), "Xóa sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            cartAdapter.updateData(new ArrayList<>(), new HashMap<>());
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        tongTien.setText(String.format("%s VND", getTotalPrice()));
    }

    private double getTotalPrice() {
        double total = 0;
        Map<Integer, List<CartProduct>> cartProductsMap = getCartProducts();
        List<CartProduct> products = cartProductsMap.getOrDefault(Integer.parseInt(userId), new ArrayList<>());
        for (CartProduct product : products) {
            total += product.getTotalPrice();
        }
        return total;
    }

    private List<Cart> getCart() {
        return new CartTableHelper(requireContext()).getCartsByUserId(Integer.parseInt(userId));
    }

    private Map<Integer, List<CartProduct>> getCartProducts() {
        return new HashMap() {{
            put(Integer.parseInt(userId), new CartProductTableHelper(requireContext()).getCartProductsByCartId(Integer.parseInt(userId)));
        }};
    }
    private void handleCheckout() {
        // Lấy danh sách sản phẩm trong giỏ hàng của người dùng
        Map<Integer, List<CartProduct>> cartProductsMap = getCartProducts();
        List<CartProduct> cartProductList = cartProductsMap.get(Integer.parseInt(userId));

        if (cartProductList == null || cartProductList.isEmpty()) {
            Toast.makeText(requireContext(), "Không có sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tính tổng giá trị sản phẩm trong giỏ
        double total = 0;
        for (CartProduct product : cartProductList) {
            total += product.getTotalPrice(); // Assuming this method exists
        }

        // Lấy dữ liệu cũ từ SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("CartData", Context.MODE_PRIVATE);
        String ordersJson = preferences.getString("orders_list", "[]");  // Nếu không có, trả về danh sách trống

        // Chuyển đổi dữ liệu JSON thành List<List<CartProduct>>
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<CartProduct>>>() {}.getType();
        List<List<CartProduct>> ordersList = gson.fromJson(ordersJson, type);

        // Nếu ordersList rỗng, khởi tạo danh sách mới
        if (ordersList == null) {
            ordersList = new ArrayList<>();
        }

        // Thêm giỏ hàng hiện tại vào ordersList
        ordersList.add(cartProductList);

        // Lưu lại danh sách mới vào SharedPreferences
        String updatedOrdersJson = gson.toJson(ordersList);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("orders_list", updatedOrdersJson);
        editor.putString("payment_method", selectedPaymentMethod);
        editor.putString("total_amount", String.valueOf(total));
        editor.putBoolean("isPay", true);
        editor.apply();

        // Xóa tất cả sản phẩm trong giỏ hàng
        deleteAllCartProducts();

        // Thông báo thanh toán thành công
        Toast.makeText(requireContext(), "Thanh toán bằng " + selectedPaymentMethod + ", vui lòng coi hóa đơn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartUpdated() {
        updateTotalPrice();
    }
}
