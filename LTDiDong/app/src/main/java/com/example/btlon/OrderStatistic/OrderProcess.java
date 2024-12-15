package com.example.btlon.OrderStatistic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.btlon.Adapter.OrderAdapter;
import com.example.btlon.Data.CartProduct;
import com.example.btlon.Data.CartProductTableHelper;
import com.example.btlon.Data.Order;
import com.example.btlon.Data.OrderTableHelper;
import com.example.btlon.Data.OrderdetailTableHelper;
import com.example.btlon.Data.ProductTableHelper;
import com.example.btlon.Data.UserTableHelper;
import com.example.btlon.Data.Users;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;


public class OrderProcess extends Fragment {
    private RecyclerView recyclerViewOrder;
    private OrderAdapter orderAdapter;
    private OrderTableHelper orderTableHelper;
    private OrderdetailTableHelper orderdetailTableHelper;
    private ProductTableHelper productTableHelper;
    private UserTableHelper userTableHelper;  // Khai báo UserTableHelper để kiểm tra quyền người dùng
    private PreferenceManager preferenceManager;  // Khai báo PreferenceManager để lấy userId

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_process, container, false);

        // Khởi tạo các helper
        orderTableHelper = new OrderTableHelper(requireContext());
        orderdetailTableHelper = new OrderdetailTableHelper(requireContext());
        productTableHelper = new ProductTableHelper(requireContext());
        userTableHelper = new UserTableHelper(requireContext());  // Khởi tạo UserTableHelper
        preferenceManager = new PreferenceManager(requireContext());  // Khởi tạo PreferenceManager

        // Khởi tạo RecyclerView
        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrderProcess);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Lấy danh sách tất cả các đơn hàng
        List<Order> orderList = orderTableHelper.getAllOrdersWithDetails();

        // Lọc các đơn hàng chỉ cho Admin/Staff và chưa thanh toán
        List<Order> filteredOrders = new ArrayList<>();
        String userRole = userTableHelper.checkRole(preferenceManager.getUserId());
        boolean isAdminOrStaff = "Admin".equals(userRole) || "Staff".equals(userRole);

        for (Order order : orderList) {
            // Nếu người dùng là Admin hoặc Staff, chỉ hiển thị đơn hàng chưa thanh toán
            if (isAdminOrStaff && order.isStatus()) {
                continue;  // Bỏ qua đơn hàng đã thanh toán
            }
            filteredOrders.add(order);  // Thêm đơn hàng vào danh sách lọc
        }

        // Cập nhật adapter với danh sách đơn hàng đã lọc
        orderAdapter = new OrderAdapter(requireContext(), filteredOrders);
        recyclerViewOrder.setAdapter(orderAdapter);
        orderAdapter.notifyDataSetChanged();

        return view;
    }
}
