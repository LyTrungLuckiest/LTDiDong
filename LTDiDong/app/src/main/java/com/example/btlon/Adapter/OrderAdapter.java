package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.Order;
import com.example.btlon.Data.OrderDetail;
import com.example.btlon.R;

import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private Map<Integer, List<OrderDetail>> orderDetailsMap; // orderId -> danh sách chi tiết
    private Context context;

    public OrderAdapter(Context context, List<Order> orders, Map<Integer, List<OrderDetail>> orderDetailsMap) {
        this.context = context;
        this.orders = orders;
        this.orderDetailsMap = orderDetailsMap;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        // Set order ID and date
        holder.tvOrderId.setText("Mã đơn hàng: #" + order.getOrderId());
        holder.tvOrderDate.setText("Ngày: " + order.getOrderDate());

        // Get the details for this order
        List<OrderDetail> details = orderDetailsMap.get(order.getOrderId());
        double totalAmount = 0.0;

        // Sum the total prices from the order details
        if (details != null) {
            for (OrderDetail detail : details) {
                totalAmount += detail.getTotalPrice();  // Sum total price for this order
            }
        }

        // Set the total amount on the order
        order.setTotalAmount(totalAmount);

        // Display the total amount with commas and the correct format
        holder.tvTotalAmount.setText("Tổng tiền: " + String.format("%,.0f VND", totalAmount));

        // Set RecyclerView for order details
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(context, details);
        holder.recyclerViewOrderDetail.setAdapter(detailAdapter);
        holder.recyclerViewOrderDetail.setLayoutManager(new LinearLayoutManager(context));
    }




    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvTotalAmount;
        RecyclerView recyclerViewOrderDetail;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            recyclerViewOrderDetail = itemView.findViewById(R.id.recyclerViewOrderDetail);
        }
    }
}

