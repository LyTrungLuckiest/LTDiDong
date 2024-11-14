package com.example.btlon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemManagerViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public ItemManagerViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            imageView = itemView.findViewById(R.id.card_admin_manager_image); // Đảm bảo ID chính xác
        }
    }
}
