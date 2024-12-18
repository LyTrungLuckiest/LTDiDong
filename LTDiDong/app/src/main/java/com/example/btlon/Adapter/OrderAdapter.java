package com.example.btlon.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Model.Order;
import com.example.btlon.Model.OrderDetail;
import com.example.btlon.Model.OrderTableHelper;
import com.example.btlon.Model.OrderdetailTableHelper;
import com.example.btlon.Model.UserTableHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;


import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private final Context context;
    private final OrderTableHelper orderTableHelper;
    private final OrderdetailTableHelper orderdetailTableHelper;
    private final UserTableHelper userTableHelper;
    private final PreferenceManager preferenceManager;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.orderTableHelper = new OrderTableHelper(context);
        this.orderdetailTableHelper = new OrderdetailTableHelper(context);
        this.userTableHelper = new UserTableHelper(context);
        this.preferenceManager = new PreferenceManager(context);
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

        // Hiển thị thông tin đơn hàng
        holder.tvOrderId.setText("Mã đơn hàng: #" + order.getOrderId());
        holder.tvOrderDate.setText("Ngày: " + order.getOrderDate());
        holder.tvState.setText(order.isStatus() ? "Đã thanh toán" : "Chờ thanh toán");

        // Tính tổng giá trị đơn hàng
        List<OrderDetail> details = order.getOrderDetails();
        double totalAmount = 0.0;
        if (details != null) {
            for (OrderDetail detail : details) {
                totalAmount += detail.getTotalPrice();
            }
        }
        holder.tvTotalAmount.setText("Tổng tiền: " + String.format("%,.0f VND", totalAmount));

        // Set RecyclerView cho chi tiết đơn hàng
        OrderDetailAdapter detailAdapter = new OrderDetailAdapter(context, details);
        holder.recyclerViewOrderDetail.setAdapter(detailAdapter);
        holder.recyclerViewOrderDetail.setLayoutManager(new LinearLayoutManager(context));

        // Xử lý quyền người dùng
        String userRole = userTableHelper.checkRole(preferenceManager.getUserId());
        boolean isAdminOrStaff = "Admin".equals(userRole) || "Staff".equals(userRole);

        if (isAdminOrStaff) {
            holder.btDeleteOrder.setText("Xác nhận thanh toán");
            holder.btDeleteOrder.setOnClickListener(view -> handleConfirmPayment(holder, order));
        } else {
            holder.btDeleteOrder.setText("Hủy đơn");
            holder.btDeleteOrder.setOnClickListener(view -> handleDeleteOrder(holder, order));
        }

        // Ẩn nút nếu đơn hàng đã thanh toán
        if (order.isStatus()) {
            holder.btDeleteOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Phương thức để cập nhật danh sách đơn hàng
    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    // Xử lý hủy đơn hàng
    private void handleDeleteOrder(OrderViewHolder holder, Order order) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition == RecyclerView.NO_POSITION) return;

        boolean isDeleted = orderTableHelper.deleteOrder(order.getOrderId());
        if (isDeleted) {
            orderdetailTableHelper.deleteOrderDetailsByOrderId(order.getOrderId());
            orders.remove(currentPosition);
            notifyItemRemoved(currentPosition);
            notifyItemRangeChanged(currentPosition, orders.size());
            Log.d("OrderAdapter", "Deleted order with ID: " + order.getOrderId());
        } else {
            Log.e("OrderAdapter", "Failed to delete order with ID: " + order.getOrderId());
        }
    }

    // Xử lý xác nhận thanh toán
    private void handleConfirmPayment(OrderViewHolder holder, Order order) {
        int currentPosition = holder.getAdapterPosition();
        if (currentPosition == RecyclerView.NO_POSITION) return;

        // Cập nhật trạng thái thanh toán trong cơ sở dữ liệu
        boolean isUpdated = orderTableHelper.updateOrderStatus(order.getOrderId(), true);

        if (isUpdated) {
            // Loại bỏ đơn hàng đã thanh toán khỏi danh sách hiển thị
            orders.remove(currentPosition);
            notifyItemRemoved(currentPosition);
            notifyItemRangeChanged(currentPosition, orders.size());
            Log.d("OrderAdapter", "Confirmed payment for order ID: " + order.getOrderId());
        } else {
            Log.e("OrderAdapter", "Failed to confirm payment for order ID: " + order.getOrderId());
        }
    }



    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvTotalAmount, tvState;
        RecyclerView recyclerViewOrderDetail;
        Button btDeleteOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvState = itemView.findViewById(R.id.tvState);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            recyclerViewOrderDetail = itemView.findViewById(R.id.recyclerViewOrderDetail);
            btDeleteOrder = itemView.findViewById(R.id.btDeleteOrder);
        }
    }
}
