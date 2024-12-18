package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Model.OrderDetail;
import com.example.btlon.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.DetailViewHolder> {
    private List<OrderDetail> orderDetails;
    private Context context;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orderdetail, parent, false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        holder.tvProductName.setText(detail.getProduct().getName());
        holder.tvQuantity.setText( detail.getQuantity()+"");

        // Use the pre-calculated totalPrice
        double totalPrice = detail.getTotalPrice();
        String formattedPrice = String.format("%,.0f VND", totalPrice);  // format price with commas
        holder.tvPrice.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvQuantity, tvPrice;

        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}

