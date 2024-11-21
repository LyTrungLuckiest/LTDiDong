package com.example.btlon.Ui.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.ItemManager;
import com.example.btlon.Adapter.ItemManagerAdapter;
import com.example.btlon.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout fragment_settings.xml
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        // Set the GridLayoutManager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Create a list of items for the RecyclerView
        List<ItemManager> itemList = new ArrayList<>();

        AddItems(itemList);

        // Set the adapter to RecyclerView
        ItemManagerAdapter adapter = new ItemManagerAdapter(itemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private static void AddItems(List<ItemManager> itemList) {
        itemList.add(new ItemManager("Người dùng", R.drawable.user_icon));
        itemList.add(new ItemManager("Danh mục sản phẩm", R.drawable.list_product));
        itemList.add(new ItemManager("Sản phẩm", R.drawable.food_add_icon));
        itemList.add(new ItemManager("Hóa đơn", R.drawable.oder_icon));
    }
}
