package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.OrderStatistic.UserOrderCount;
import com.example.btlon.R;


import java.util.List;

public class OrderByUserAdapter extends RecyclerView.Adapter<OrderByUserAdapter.OrderViewHolder> {

    private Context context;
    private List<UserOrderCount> userOrderCountList;

    // Constructor
    public OrderByUserAdapter(Context context, List<UserOrderCount> userOrderCountList) {
        this.context = context;
        this.userOrderCountList = userOrderCountList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_orderbyuser, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Bind data to views
        UserOrderCount userOrderCount=userOrderCountList.get(position);

        holder.tvUserId.setText(userOrderCount.getUserId()+"");
        holder.tvTotalOrder.setText(userOrderCount.getTotalOrders()+"");

    }

    @Override
    public int getItemCount() {
        return userOrderCountList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserId, tvTotalOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvTotalOrder = itemView.findViewById(R.id.tvTotalOrder);

        }
    }
}

