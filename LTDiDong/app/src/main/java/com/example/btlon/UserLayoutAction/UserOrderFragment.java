package com.example.btlon.UserLayoutAction;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.btlon.Adapter.OrderAdapter;
import com.example.btlon.Model.CartProduct;
import com.example.btlon.Model.CartProductTableHelper;
import com.example.btlon.Model.Order;
import com.example.btlon.Model.OrderDetail;
import com.example.btlon.Model.OrderTableHelper;
import com.example.btlon.Model.OrderdetailTableHelper;
import com.example.btlon.Model.Product;
import com.example.btlon.Model.ProductTableHelper;
import com.example.btlon.Model.UserTableHelper;
import com.example.btlon.Model.Users;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserOrderFragment extends Fragment {

    private RecyclerView recyclerViewOrder;
    private OrderAdapter orderAdapter;

    private CartProductTableHelper cartProductTableHelper;
    private UserTableHelper userTableHelper;
    private OrderTableHelper orderTableHelper;
    private OrderdetailTableHelper orderdetailTableHelper;
    private PreferenceManager preferenceManager;
    private String userId;
    private List<CartProduct> cartProductArrayList;
    private ProductTableHelper productTableHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_order, container, false);

        // Khởi tạo các helper
        userTableHelper = new UserTableHelper(requireContext());
        cartProductTableHelper = new CartProductTableHelper(requireContext());
        orderTableHelper = new OrderTableHelper(requireContext());
        preferenceManager = new PreferenceManager(requireContext());
        userId = preferenceManager.getUserId();
        Users currentUser = userTableHelper.getUserById(Integer.parseInt(userId));
        cartProductArrayList = new ArrayList<>();
        orderdetailTableHelper = new OrderdetailTableHelper(requireContext());
        productTableHelper = new ProductTableHelper(requireContext());


        // Khởi tạo RecyclerView
        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("CartData", Context.MODE_PRIVATE);
        String ordersJson = preferences.getString("orders_list", "[]"); // Nếu không có dữ liệu, trả về một danh sách trống
        Log.d("UserOrderFragment", "Dữ liệu ordersJson: " + ordersJson);

        // Chuyển đổi dữ liệu JSON thành List<List<CartProduct>>
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<CartProduct>>>() {}.getType();
        List<List<CartProduct>> ordersList = gson.fromJson(ordersJson, type);




        // Kiểm tra xem ordersList có phần tử không
        if (ordersList != null && !ordersList.isEmpty()) {
            // Duyệt qua danh sách ordersList bằng cách sử dụng Iterator
            Iterator<List<CartProduct>> iterator = ordersList.iterator();
            while (iterator.hasNext()) {
                List<CartProduct> order = iterator.next();
                cartProductArrayList = order;  // Gán từng đơn hàng vào cartProductArrayList

                // Get payment and total amount from SharedPreferences
                boolean isPay = preferences.getBoolean("isPay", false);
                String paymentMethod = preferences.getString("payment_method", "");
                double totalAmount = Double.parseDouble(preferences.getString("total_amount", "0"));

                if (isPay && cartProductArrayList != null && !cartProductArrayList.isEmpty()) {
                    // Chuyển đổi danh sách CartProduct thành danh sách OrderDetail
                    List<OrderDetail> orderDetails = new ArrayList<>();
                    for (CartProduct cartProduct : cartProductArrayList) {
                        orderDetails.add(new OrderDetail(cartProduct.getProduct(), cartProduct.getQuantity()));
                    }

                    // Lưu đơn hàng vào cơ sở dữ liệu
                    int orderId = orderTableHelper.addOrder(new Order(currentUser, orderDetails));
                    for (OrderDetail orderDetail : orderDetails) {
                        orderdetailTableHelper.addOrderDetail(orderId, orderDetail);
                    }

                    Log.d("UserOrderFragment", "Đơn hàng đã được lưu vào cơ sở dữ liệu.");

                    // Sau khi lưu đơn hàng vào cơ sở dữ liệu, xóa đơn hàng khỏi ordersList
                    iterator.remove(); // Xóa đơn hàng đã xử lý khỏi ordersList

                    // Cập nhật lại SharedPreferences để lưu danh sách đơn hàng mới (nếu còn đơn hàng)
                     gson = new Gson();
                    String updatedOrdersJson = gson.toJson(ordersList); // Chuyển đổi danh sách đơn hàng còn lại thành JSON

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("orders_list", updatedOrdersJson); // Lưu lại danh sách đơn hàng mới
                    editor.apply();

                    Log.d("UserOrderFragment", "Đã cập nhật lại danh sách đơn hàng trong SharedPreferences.");

                } else {
                    Log.d("UserOrderFragment", "Chưa thanh toán, không thực hiện lưu đơn hàng.");
                }
            }
        } else {
            Log.e("UserOrderFragment", "ordersList rỗng hoặc không có phần tử");
        }



// Cập nhật trạng thái isPay để tránh lưu lại đơn hàng khi quay lại ứng dụng
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isPay", false);  // Đánh dấu đã xử lý thanh toán
        editor.apply();





        // Lấy danh sách các đơn hàng và hiển thị lên RecyclerView
        List<Order> orders = getOrders(currentUser);
        // Log tất cả orderId trong danh sách orders
        for (Order order : orders) {
            Log.d("OrderList", "Order ID: " + order.getOrderId());
        }
        orderAdapter = new OrderAdapter(requireContext(), orders);
        recyclerViewOrder.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();


        //reset oder and oder detail
//        orderTableHelper.deleteAllOrders();
//        orderdetailTableHelper.deleteAllOrderDetails();
//        clearOrdersList();
        return view;
    }


    // Hàm lấy danh sách các đơn hàng từ cơ sở dữ liệu
    private List<Order> getOrders(Users user) {
        List<Order> orders = new ArrayList<>();
        List<Order> unpaidOrders = new ArrayList<>();
        List<Order> paidOrders = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = orderTableHelper.getOrdersForUser(user.getUserId());

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int orderIdIndex = cursor.getColumnIndex("order_id");
                    int orderDateIndex = cursor.getColumnIndex("order_date");
                    int orderStatusIndex = cursor.getColumnIndex("status");

                    if (orderIdIndex >= 0 && orderDateIndex >= 0 && orderStatusIndex >= 0) {
                        int orderId = cursor.getInt(orderIdIndex);
                        String orderDate = cursor.getString(orderDateIndex);
                        boolean orderStatus = cursor.getInt(orderStatusIndex) == 1;

                        List<OrderDetail> orderDetails = getOrderDetails(orderId);
                        Order order = new Order(orderId, user, orderDate, orderStatus, orderDetails);

                        // Phân loại hóa đơn
                        if (!orderStatus) {
                            unpaidOrders.add(order); // Hóa đơn chưa thanh toán
                        } else {
                            paidOrders.add(order); // Hóa đơn đã thanh toán
                        }
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Cursor", "Error while processing cursor: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        // Gộp danh sách: hóa đơn chưa thanh toán trước, đã thanh toán sau
        orders.addAll(unpaidOrders);
        orders.addAll(paidOrders);

        return orders;
    }




    // Hàm lấy các chi tiết đơn hàng
    private List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Cursor cursor = orderdetailTableHelper.getOrderDetailsByOrderId(orderId);
        if (cursor != null && cursor.moveToFirst()) {
            int productIdIndex = cursor.getColumnIndex("product_id");
            int quantityIndex = cursor.getColumnIndex("quantity");

            // Log column indexes to check for validity
            Log.d("Cursor", "product_id index: " + productIdIndex);
            Log.d("Cursor", "quantity index: " + quantityIndex);

            // Ensure indexes are valid (≥ 0)
            if (productIdIndex >= 0 && quantityIndex >= 0) {
                do {
                    int productId = cursor.getInt(productIdIndex);
                    int quantity = cursor.getInt(quantityIndex);

                    // Lấy thông tin sản phẩm từ cơ sở dữ liệu
                    Product product = productTableHelper.getProductById(productId);

                    // Thêm chi tiết đơn hàng vào danh sách
                    orderDetails.add(new OrderDetail(orderId, product, quantity));
                } while (cursor.moveToNext());
            } else {
                Log.e("Cursor", "Invalid column indexes.");
            }
            cursor.close();
        }
        return orderDetails;
    }

    private void clearOrdersList() {
        // Lấy SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("CartData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Xóa tất cả dữ liệu trong ordersList
        editor.remove("orders_list");  // Xóa dữ liệu cũ trong SharedPreferences

        // Cập nhật lại giá trị của các thuộc tính cần thiết (nếu cần)
        editor.putBoolean("isPay", false); // Đánh dấu là chưa thanh toán
        editor.apply();  // Lưu các thay đổi vào SharedPreferences

        Log.d("UserOrderFragment", "Đã xóa dữ liệu đơn hàng khỏi SharedPreferences.");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Button btUserOrderBack = view.findViewById(R.id.btUserOrderBack);
            if (btUserOrderBack != null) {
                NavController navController = Navigation.findNavController(view);
                btUserOrderBack.setOnClickListener(v -> navController.popBackStack());
            }
        } catch (IllegalStateException e) {
            Log.e("NavigationError", "NavController không tìm thấy: " + e.getMessage());
        }
    }
}
