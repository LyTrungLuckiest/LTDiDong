package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.OrderStatistic.CategoryOrderCount;
import com.example.btlon.R;

import java.util.List;

public class OrderByCategoryAdapter extends RecyclerView.Adapter<OrderByCategoryAdapter.OrderViewHolder> {
    private Context context;
    private List<CategoryOrderCount> categoryOrderCounts;

    // Constructor
    public OrderByCategoryAdapter(Context context, List<CategoryOrderCount> categoryOrderCounts) {
        this.context = context;
        this.categoryOrderCounts = categoryOrderCounts;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_by_category, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Bind data to views
        CategoryOrderCount categoryOrderCount=categoryOrderCounts.get(position);

        holder.tvCategoryId.setText(categoryOrderCount.getCategoryId()+"");
        holder.tvTotalOrder.setText(categoryOrderCount.getTotalOrders()+"");
        holder.tvMostSellingProduct.setText(categoryOrderCount.getMostSellingProductName()+"");

    }

    @Override
    public int getItemCount() {
        return categoryOrderCounts.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryId, tvTotalOrder,tvMostSellingProduct;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            tvCategoryId = itemView.findViewById(R.id.tvCategoryIdOrderByCategory);
            tvTotalOrder = itemView.findViewById(R.id.tvTotalOrderOrderbyCategory);
            tvMostSellingProduct=itemView.findViewById(R.id.tvMostSellingProduct);

        }
    }
}


