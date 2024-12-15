package com.example.btlon.Ui.Home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.EditText;
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
import com.example.btlon.Data.Product;
import com.example.btlon.Data.ProductTableHelper;
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
    private TextView txtGioHangTrong, txtTongTien;
    private RecyclerView recyclerView;
    private Button btnThanhToan, btnXoaAll;
    private Spinner spinnerPhuongThucThanhToan;
    private CartAdapter cartAdapter;
    private String userId, selectedPaymentMethod = "Tiền mặt";
    private PreferenceManager preferenceManager;
    private EditText edtNhapDiaChi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX); // Khởi tạo SDK ZaloPay
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_cart_fragment, container, false);

        preferenceManager = new PreferenceManager(requireContext());
        userId = preferenceManager.getUserId();

        if (!preferenceManager.isLoggedIn() || TextUtils.isEmpty(userId)) {
            Toast.makeText(requireContext(), "Bạn cần đăng nhập để tiếp tục!", Toast.LENGTH_SHORT).show();
            ScrollView scrollView = view.findViewById(R.id.scrollviewCart);
            scrollView.setVisibility(View.GONE); // Ẩn giỏ hàng nếu chưa đăng nhập
            return view;
        }

        initializeUI(view);
        setRecyclerViewAdapter(getCart(), getCartProducts());

        setupSpinner();
        updateTotalPrice(); // Cập nhật tổng tiền khi hiển thị giỏ hàng



        return view;
    }



    private PackageManager getPackageManager() {
        return requireContext().getPackageManager();
    }


    private void initializeUI(View view) {
        txtGioHangTrong = view.findViewById(R.id.txtgiohangtrong2);
        txtTongTien = view.findViewById(R.id.txttongtien);
        recyclerView = view.findViewById(R.id.recyclerviewCart);
        btnThanhToan = view.findViewById(R.id.btntienhang);
        btnXoaAll = view.findViewById(R.id.btnXoaAll);
        spinnerPhuongThucThanhToan = view.findViewById(R.id.spinnerPaymentMethod);

        btnThanhToan.setOnClickListener(v -> handlePayment());
        btnXoaAll.setOnClickListener(v -> deleteAllCartProducts());
        edtNhapDiaChi = view.findViewById(R.id.edtnhapdiachi); //
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Tiền mặt", "MoMo", "ZaloPay", "Ngân hàng"});
        spinnerPhuongThucThanhToan.setAdapter(adapter);
        spinnerPhuongThucThanhToan.setSelection(0);
        spinnerPhuongThucThanhToan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        String address = edtNhapDiaChi.getText().toString().trim(); // Lấy địa chỉ nhập vào
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(requireContext(), "Vui lòng nhập địa chỉ giao hàng!", Toast.LENGTH_SHORT).show();
            return;
        }
        double total = Double.parseDouble(txtTongTien.getText().toString().replace("VND", "").trim());
        if (total <= 0) {
            Toast.makeText(requireContext(), "Giỏ hàng của bạn đang trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (selectedPaymentMethod) {
            case "Tiền mặt":
                navigateToResult("Thanh toán tiền mặt thành công!");
                handleCheckout();
                break;
            case "MoMo":
                navigateToResult("Thanh toán MoMo thành công!");
                break;
            case "ZaloPay":
                handleZaloPayPayment(total);
//                navigateToResult("Thanh toán ZaloPay thành công!");
                break;
            case "Ngân hàng":
                handleNHPayment(total);

//                navigateToResult("Thanh toán Ngân hàng thành công!");
                break;
            default:
                Toast.makeText(requireContext(), "Phương thức chưa hỗ trợ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNHPayment(double total) {
        String accountNumber = "5811322678";
        String amount = String.valueOf(total);
        String bankDeeplink = "https://dl.vietqr.io/pay?app=bibv&account=" + accountNumber + "&amount=" + amount;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bankDeeplink));
        startActivity(intent);


// Kiểm tra nếu có ứng dụng hỗ trợ mở deeplink này
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            handleCheckout();
        } else {
            Toast.makeText(requireContext(), "Không tìm thấy ứng dụng.", Toast.LENGTH_SHORT).show();
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
                        handleCheckout();
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

    private void setupButtonListeners() {
        btnThanhToan.setOnClickListener(v -> handlePayment());
    }

    private void handleCheckout() {
        // Lấy danh sách sản phẩm trong giỏ hàng
        Map<Integer, List<CartProduct>> cartProductsMap = getCartProducts();
        List<CartProduct> cartProductList = cartProductsMap.get(Integer.parseInt(userId));

        if (cartProductList == null || cartProductList.isEmpty()) {
            Toast.makeText(requireContext(), "Không có sản phẩm để thanh toán!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tính tổng giá trị sản phẩm trong giỏ
        double total = 0;
        for (CartProduct product : cartProductList) {
            total += product.getTotalPrice();

            // Cập nhật số lượng trong Database
            updateProductQuantity(product.getProduct().getId(), product.getQuantity());
        }

        // Lấy dữ liệu cũ từ SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("CartData", Context.MODE_PRIVATE);
        String ordersJson = preferences.getString("orders_list", "[]");

        // Chuyển đổi dữ liệu JSON thành List<List<CartProduct>>
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<CartProduct>>>() {
        }.getType();
        List<List<CartProduct>> ordersList = gson.fromJson(ordersJson, type);

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
    private void updateProductQuantity(int productId, int purchasedQuantity) {
        ProductTableHelper productTableHelper = new ProductTableHelper(getContext());

        // Lấy sản phẩm từ Database
        Product product = productTableHelper.getProductById(productId);

        if (product != null) {
            int newStockQuantity = product.getStockQuantity() - purchasedQuantity;
            int newSoldQuantity = product.getSoldQuantity() + purchasedQuantity;

            // Kiểm tra nếu số lượng tồn kho >= 0
            if (newStockQuantity < 0) {
                Toast.makeText(requireContext(), "Số lượng sản phẩm không đủ!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật số lượng mới vào Database
            productTableHelper.updateProductQuantity(productId, newStockQuantity, newSoldQuantity);
        } else {
            Log.e("CartFragment", "Không tìm thấy sản phẩm với ID: " + productId);
        }
    }


    private void deleteAllCartProducts() {
        CartTableHelper cartTableHelper = new CartTableHelper(getContext());
        boolean isDeleted = cartTableHelper.deleteCartProductsByCartId(Integer.parseInt(userId));

        if (isDeleted) {
            Log.d("CartFragment", "Tất cả sản phẩm trong giỏ đã được xóa thành công.");
            updateUIAfterDeletion();
        } else {
            Log.e("CartFragment", "Xóa sản phẩm trong giỏ thất bại.");
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
                total += product.getTotalPrice();
            }
        }

        txtTongTien.setText(String.format("%s VND", total));
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

    @Override
    public void onCartUpdated() {
        List<Cart> updatedCarts = getCart();
        Map<Integer, List<CartProduct>> updatedCartProductsMap = getCartProducts();
        cartAdapter.updateData(updatedCarts, updatedCartProductsMap);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }
}
