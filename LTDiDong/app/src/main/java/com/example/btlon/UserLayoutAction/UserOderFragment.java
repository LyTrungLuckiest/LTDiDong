package com.example.btlon.UserLayoutAction;

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
import com.example.btlon.Data.Order;
import com.example.btlon.Data.OrderDetail;
import com.example.btlon.Data.Product;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserOderFragment extends Fragment {

    private RecyclerView recyclerViewOrder;
    private OrderAdapter orderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_oder, container, false);

        // Khởi tạo RecyclerView
        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Lấy danh sách đơn hàng và chi tiết đơn hàng
        List<Order> orders = getOrders(); // Lấy danh sách đơn hàng
        Map<Integer, List<OrderDetail>> orderDetailsMap = getOrderDetails(); // Lấy chi tiết đơn hàng


        // Cài đặt Adapter
        orderAdapter = new OrderAdapter(requireContext(), orders, orderDetailsMap);
        recyclerViewOrder.setAdapter(orderAdapter);

        return view;
    }

    // Hàm lấy danh sách đơn hàng (giả lập dữ liệu)
    private List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        // Giả lập thông tin đơn hàng
        orders.add(new Order(1, null, "2024-11-27",  new ArrayList<>())); // Giả sử Users là null cho ví dụ này
        orders.add(new Order(2, null, "2024-11-26", new ArrayList<>()));
        return orders;
    }

    // Hàm lấy chi tiết đơn hàng (giả lập dữ liệu)
    private Map<Integer, List<OrderDetail>> getOrderDetails() {
        Map<Integer, List<OrderDetail>> map = new HashMap<>();

        // Chi tiết cho Order 1
        List<OrderDetail> order1Details = new ArrayList<>();
        order1Details.add(new OrderDetail(1,new Product(100,"San Pham A","10000"), 1));
        order1Details.add(new OrderDetail(2,new Product(101,"San Pham B","20000"),2));
        map.put(1, order1Details);

        // Chi tiết cho Order 2
        List<OrderDetail> order2Details = new ArrayList<>();
        order2Details.add(new OrderDetail(1,new Product(102,"San Pham C","30000"), 3));
        order2Details.add(new OrderDetail(2,new Product(103,"San Pham D","40000"),4));
        map.put(2, order2Details);

        return map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            Button btUserAddressBack = view.findViewById(R.id.btUserOrderBack);
            if (btUserAddressBack != null) {
                NavController navController = Navigation.findNavController(view);
                btUserAddressBack.setOnClickListener(v -> navController.popBackStack());
            }
        } catch (IllegalStateException e) {
            Log.e("NavigationError", "NavController không tìm thấy: " + e.getMessage());
        }
    }
}
