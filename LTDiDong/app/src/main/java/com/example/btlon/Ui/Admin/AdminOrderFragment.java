package com.example.btlon.Ui.Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.btlon.Adapter.ItemAdminOrderAdapter;
import com.example.btlon.Model.ItemAdminOrder;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderFragment extends Fragment {

    private Button btBack;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_order, container, false);

        btBack = view.findViewById(R.id.btBackToSettings);


        // Sự kiện cho nút "Back"
        btBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack();
        });


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAdminOrder);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<ItemAdminOrder> itemList = new ArrayList<>();

        AddOrderItems(itemList);

        ItemAdminOrderAdapter adapter = new ItemAdminOrderAdapter(itemList);
        recyclerView.setAdapter(adapter);


        return view;
    }


    private static void AddOrderItems(List< ItemAdminOrder > itemList) {
        itemList.add(new ItemAdminOrder("Xử lý hóa đơn"));
        itemList.add(new ItemAdminOrder("Thống kê hóa đơn theo người dùng"));
        itemList.add(new ItemAdminOrder("Thống kê hóa đơn theo ngày"));
        itemList.add(new ItemAdminOrder("Thống kê hóa đơn theo danh mục sản phẩm"));

    }

}