package com.example.btlon.Adapter;

import android.content.Context;
import android.util.Log;
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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders; // Danh sách các đơn hàng
    private Context context;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
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
        Log.d("OrderAdapter", "Binding order with ID: " + order.getOrderId());
        // Hiển thị mã đơn hàng và ngày
        holder.tvOrderId.setText("Mã đơn hàng: #" + order.getOrderId());
        holder.tvOrderDate.setText("Ngày: " + order.getOrderDate());

        // Lấy danh sách chi tiết đơn hàng
        List<OrderDetail> details = order.getOrderDetails();  // Lấy trực tiếp từ Order

        double totalAmount = 0.0;

        // Tính tổng giá trị đơn hàng từ chi tiết
        if (details != null) {
            for (OrderDetail detail : details) {
                totalAmount += detail.getTotalPrice();  // Tổng giá trị từ chi tiết đơn hàng
            }
        }

        // Hiển thị tổng tiền
        holder.tvTotalAmount.setText("Tổng tiền: " + String.format("%,.0f VND", totalAmount));

        // Set RecyclerView cho chi tiết đơn hàng
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(context, details);
        holder.recyclerViewOrderDetail.setAdapter(detailAdapter);
        holder.recyclerViewOrderDetail.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
    // Phương thức để cập nhật danh sách đơn hàng
    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;  // Cập nhật danh sách đơn hàng mới
        notifyDataSetChanged();  // Thông báo cho adapter rằng dữ liệu đã thay đổi
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