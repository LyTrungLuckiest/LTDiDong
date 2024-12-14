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

import com.example.btlon.Adapter.OrderByDateAdapter;
import com.example.btlon.Adapter.OrderByUserAdapter;
import com.example.btlon.Data.OrderTableHelper;
import com.example.btlon.R;

import java.util.List;

public class OrderByDate extends Fragment {
    private RecyclerView recyclerView;
    private OrderByDateAdapter adapter;
    private OrderTableHelper orderTableHelper;
    private Button btBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order_by_date, container, false);
        // Inflate the layout for this fragment
        recyclerView = view.findViewById(R.id.recyclerViewOrderByDate);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        orderTableHelper = new OrderTableHelper(requireContext());

        List<DateOrderCount> dateOrderCountList = orderTableHelper.getOrdersGroupedByDate();

        adapter = new OrderByDateAdapter(requireContext(),dateOrderCountList);
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