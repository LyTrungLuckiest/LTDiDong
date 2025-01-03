package com.example.btlon.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.R;
import com.example.btlon.Model.ItemManager;
import com.example.btlon.Utils.PreferenceManager;

import java.util.List;

public class ItemManagerAdapter extends RecyclerView.Adapter<ItemManagerAdapter.ItemManagerViewHolder> {

    private List<ItemManager> itemList;
    Context context;

    public ItemManagerAdapter(List<ItemManager> itemList,Context context) {
        this.context=context;
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
            PreferenceManager preferenceManager = new PreferenceManager(v.getContext());
            String role= preferenceManager.getUserRole();

            Bundle bundle = new Bundle();
            bundle.putString("itemId", item.getTitle());
            switch (item.getTitle()) {
                case "Người dùng":
                    if(role.equals("Admin")){
                    navController.navigate(R.id.action_settingsFragment_to_adminUserSettingFragment, bundle);}
                    else{
                        Toast.makeText(context,"Phải có quền admin",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Sản phẩm":
                    if(role.equals("Admin")){
                    navController.navigate(R.id.action_settingsFragment_to_adminProductSettingFragment, bundle);}
                    else{
                        Toast.makeText(context,"Phải có quền admin",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Hóa đơn":
                    if(role.equals("Admin")||role.equals("Staff")){
                    navController.navigate(R.id.action_settingsFragment_to_adminOrderFragment, bundle);}
                    break;
                case "Danh mục sản phẩm":
                    if(role.equals("Admin")) {
                        navController.navigate(R.id.action_settingsFragment_to_adminCategorySettingFragment, bundle);}
                    else{
                        Toast.makeText(context,"Phải có quền admin",Toast.LENGTH_SHORT).show();
                    }
                        break;

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
