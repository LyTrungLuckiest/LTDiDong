package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.Order;
import com.example.btlon.OrderStatistic.DateOrderCount;
import com.example.btlon.OrderStatistic.UserOrderCount;
import com.example.btlon.R;


import java.util.List;

public class OrderByDateAdapter extends RecyclerView.Adapter<OrderByDateAdapter.OrderViewHolder> {

    private Context context;
    private List<DateOrderCount> dateOrderCountList;

    // Constructor
    public OrderByDateAdapter(Context context, List<DateOrderCount> dateOrderCountList) {
        this.context = context;
        this.dateOrderCountList = dateOrderCountList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_by_date, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Bind data to views
        DateOrderCount dateOrderCount=dateOrderCountList.get(position);

        holder.tvDateOrderByDate.setText(dateOrderCount.getDate());
        holder.tvTotalOrderByOrderDate.setText(dateOrderCount.getTotalOrders()+"");

    }

    @Override
    public int getItemCount() {
        return dateOrderCountList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvDateOrderByDate, tvTotalOrderByOrderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            tvDateOrderByDate = itemView.findViewById(R.id.tvDateOrderByDate);
            tvTotalOrderByOrderDate = itemView.findViewById(R.id.tvTotalOrderOrderbydate);

        }
    }
}

