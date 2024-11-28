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

import com.example.btlon.R;
import com.example.btlon.Data.ItemManager;

import java.util.List;

public class ItemManagerAdapter extends RecyclerView.Adapter<ItemManagerAdapter.ItemManagerViewHolder> {

    private List<ItemManager> itemList;

    public ItemManagerAdapter(List<ItemManager> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_manager, parent, false);
        return new ItemManagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemManagerViewHolder holder, int position) {
        ItemManager item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        holder.imageView.setImageResource(item.getImageResId());

        holder.cardView.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            Bundle bundle = new Bundle();
            bundle.putString("itemId", item.getTitle());
            switch (item.getTitle()) {
                case "Người dùng":
                    navController.navigate(R.id.action_settingsFragment_to_adminUserSettingFragment, bundle);
                    break;
                case "Sản phẩm":
                    navController.navigate(R.id.action_settingsFragment_to_adminProductSettingFragment, bundle);
                    break;
//                case "Hóa đơn":
//                    navController.navigate(R.id.action_settingsFragment_to_adminOrderSettingFragment, bundle);
//                    break;
//                case "Danh mục sản phẩm":
//                    navController.navigate(R.id.action_settingsFragment_to_adminCategorySettingFragment, bundle);
//                    break;
//
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemManagerViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        CardView cardView;

        public ItemManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.card_admin_manager_image);
            cardView = itemView.findViewById(R.id.card_admin_manager);
        }
    }
}
