package com.example.btlon.OrderStatistic;

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

import com.example.btlon.Adapter.OrderByCategoryAdapter;
import com.example.btlon.Adapter.OrderByDateAdapter;
import com.example.btlon.Data.OrderTableHelper;
import com.example.btlon.R;

import java.util.List;

public class OrderByCategory extends Fragment {


    private RecyclerView recyclerView;
    private OrderByCategoryAdapter adapter;
    private OrderTableHelper orderTableHelper;
    private Button btBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_order_by_category, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewOrderByCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        orderTableHelper = new OrderTableHelper(requireContext());

        List<CategoryOrderCount> categoryOrderCounts = orderTableHelper.getOrdersGroupedByCategory();


        adapter = new OrderByCategoryAdapter(requireContext(),categoryOrderCounts);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            btBack = view.findViewById(R.id.btBackOrderByDate);
            if (btBack != null) {
                NavController navController = Navigation.findNavController(view);
                btBack.setOnClickListener(v -> navController.popBackStack());
            }
        } catch (IllegalStateException e) {
            Log.e("NavigationError", "NavController không tìm thấy: " + e.getMessage());
        }
    }
}