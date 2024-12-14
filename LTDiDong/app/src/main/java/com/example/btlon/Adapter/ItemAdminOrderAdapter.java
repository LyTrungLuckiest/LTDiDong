package com.example.btlon.Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.ItemAdminOrder;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;

import java.util.List;

public class ItemAdminOrderAdapter extends RecyclerView.Adapter<ItemAdminOrderAdapter.ItemAdminOrderViewHolder> {

    private List<ItemAdminOrder> itemList;

    public ItemAdminOrderAdapter(List<ItemAdminOrder> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemAdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_mananager_option, parent, false);
        return new ItemAdminOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdminOrderViewHolder holder, int position) {
        ItemAdminOrder item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());

        holder.cardView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            PreferenceManager preferenceManager = new PreferenceManager(v.getContext());
            String role = preferenceManager.getUserRole();

            Bundle bundle = new Bundle();
            bundle.putString("itemOrderId", item.getTitle());

            switch (item.getTitle()) {
                case "Xử lý hóa đơn":
                    navController.navigate(R.id.action_adminOrderFragment_to_orderProcess, bundle);
                    break;
                case "Thống kê hóa đơn theo người dùng":
                    navController.navigate(R.id.action_adminOrderFragment_to_orderByUser, bundle);
                    break;
                case "Thống kê hóa đơn theo ngày":
                    navController.navigate(R.id.action_adminOrderFragment_to_orderByDate, bundle);
                    break;
                case "Thống kê hóa đơn theo danh mục sản phẩm":
                    navController.navigate(R.id.action_adminOrderFragment_to_orderByCategory, bundle);
                    break;
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemAdminOrderViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        CardView cardView;

        public ItemAdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            cardView = itemView.findViewById(R.id.card_admin_order);
        }
    }

}
