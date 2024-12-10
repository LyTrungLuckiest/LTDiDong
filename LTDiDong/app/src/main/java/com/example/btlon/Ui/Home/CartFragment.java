package com.example.btlon.Ui.Home;

import android.content.Context;
import android.content.Intent;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
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

        // Initialize components
        preferenceManager = new PreferenceManager(requireContext());
        userId = preferenceManager.getUserId();

        // Kiểm tra trạng thái đăng nhập
        if (!preferenceManager.isLoggedIn() || TextUtils.isEmpty(userId)) {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập để tiếp tục!", Toast.LENGTH_SHORT).show();
            ScrollView scrollView = view.findViewById(R.id.scrollviewCart);
            scrollView.setVisibility(View.GONE);
            return view;
        }

        initializeUI(view);
        setRecyclerViewAdapter(getCart(), getCartProducts());
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

        btnXoaAll.setOnClickListener(v -> deleteAllCartProducts());
        btnThanhToan.setOnClickListener(v -> handlePayment());
    }

    private void setRecyclerViewAdapter(List<Cart> carts, Map<Integer, List<CartProduct>> cartProductsMap) {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cartAdapter = new CartAdapter(requireContext(), carts, cartProductsMap, this);
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
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
                handleCashPayment();
                break;
            case "MoMo":
                handleMoMoPayment(total);
                break;
            case "ZaloPay":
                handleZaloPayPayment(total);
                break;
            case "Ngân hàng":
                handleBankPayment(total);
                break;
            default:
                Toast.makeText(requireContext(), "Phương thức thanh toán không hợp lệ!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void handleCashPayment() {
        Toast.makeText(requireContext(), "Thanh toán bằng tiền mặt đã được chọn!", Toast.LENGTH_SHORT).show();
        navigateToResult("Thanh toán tiền mặt thành công!");
    }

    private void handleMoMoPayment(double total) {
        // Tích hợp MoMo SDK hoặc API tại đây
        Toast.makeText(requireContext(), "Thanh toán bằng MoMo đang được thực hiện...", Toast.LENGTH_SHORT).show();
        navigateToResult("Thanh toán MoMo thành công!");
    }

    private void handleZaloPayPayment(double total) {
        try {
            CreateOrder orderApi = new CreateOrder();
            JSONObject data = orderApi.createOrder(String.valueOf((int) total));
            String code = data.getString("return_code");

            if ("1".equals(code)) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(requireActivity(), token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String transactionId, String zpTransId, String message) {
                        navigateToResult("Thanh toán thành công!");
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String message) {
                        navigateToResult("Thanh toán đã bị hủy!");
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String message) {
                        navigateToResult("Lỗi hệ thống, vui lòng thử lại sau!");
                    }
                });
            } else {
                Toast.makeText(requireContext(), "Lỗi tạo đơn hàng: " + data.optString("return_message"), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("CartFragment", "Error during ZaloPay payment", e);
            Toast.makeText(requireContext(), "Lỗi hệ thống, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleBankPayment(double total) {
        // Tích hợp API ngân hàng tại đây
        Toast.makeText(requireContext(), "Thanh toán qua ngân hàng đang được thực hiện...", Toast.LENGTH_SHORT).show();
        navigateToResult("Thanh toán ngân hàng thành công!");
    }

    private void navigateToResult(String result) {
        Intent intent = new Intent(requireActivity(), PaymenActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }

    private void deleteAllCartProducts() {
        CartTableHelper cartTableHelper = new CartTableHelper(requireContext());
        boolean isDeleted = cartTableHelper.deleteCartProductsByCartId(Integer.parseInt(userId));

        if (isDeleted) {
            Toast.makeText(requireContext(), "Xóa tất cả sản phẩm thành công!", Toast.LENGTH_SHORT).show();
            updateUIAfterDeletion();
        } else {
            Toast.makeText(requireContext(), "Xóa sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUIAfterDeletion() {
        cartAdapter.updateData(new ArrayList<>(), new HashMap<>());
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double totalPrice = getTotalPrice();
        tongTien.setText(String.format("%s VND", totalPrice));
    }

    private double getTotalPrice() {
        double total = 0;
        Map<Integer, List<CartProduct>> cartProductsMap = getCartProducts();
        List<CartProduct> cartProductList = cartProductsMap.get(Integer.parseInt(userId));

        if (cartProductList != null) {
            for (CartProduct product : cartProductList) {
                total += product.getTotalPrice();
            }
        }
        return total;
    }

    private List<Cart> getCart() {
        CartTableHelper cartTableHelper = new CartTableHelper(requireContext());
        return cartTableHelper.getCartsByUserId(Integer.parseInt(userId));
    }

    private Map<Integer, List<CartProduct>> getCartProducts() {
        CartProductTableHelper cartProductTableHelper = new CartProductTableHelper(requireContext());
        Map<Integer, List<CartProduct>> map = new HashMap<>();
        map.put(Integer.parseInt(userId), cartProductTableHelper.getCartProductsByCartId(Integer.parseInt(userId)));
        return map;
    }

    @Override
    public void onCartUpdated() {
        updateTotalPrice();
    }
}
