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

import com.example.btlon.Data.Order;
import com.example.btlon.Data.OrderDetail;
import com.example.btlon.Data.OrderTableHelper;
import com.example.btlon.Data.OrderdetailTableHelper;
import com.example.btlon.R;
import com.example.btlon.Utils.PreferenceManager;

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
        OrderTableHelper orderTableHelper = new OrderTableHelper(context);
        OrderdetailTableHelper orderdetailTableHelper= new OrderdetailTableHelper(context);
        Log.d("OrderAdapter", "Binding order with ID: " + order.getOrderId());
        // Hiển thị mã đơn hàng và ngày
        holder.tvOrderId.setText("Mã đơn hàng: #" + order.getOrderId());
        holder.tvOrderDate.setText("Ngày: " + order.getOrderDate());
        holder.tvState.setText(order.isStatus()?"Đã thanh toán":"Chờ thanh toán");
        if(!order.isStatus()){
            holder.btDeleteOrder.setText("Hủy đơn");
        }else holder.btDeleteOrder.setVisibility(View.GONE);

        Log.d("OrderAdapter", "check date: " + order.getOrderDate());

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

        //Xử lý đổi lại text cho hóa đơn ở nút delete thành xác nhận khi la admin hoặc staff (đơn hàng đến nơi ng dùng đẫ thanh toán
        if(holder.btDeleteOrder.getText().toString().equals("Hủy đơn")){
            holder.btDeleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPosition = holder.getAdapterPosition(); // Lấy vị trí hiện tại của ViewHolder
                    if (currentPosition == RecyclerView.NO_POSITION) {
                        return; // Nếu vị trí không hợp lệ, thoát khỏi phương thức
                    }

                    Order currentOrder = orders.get(currentPosition);

                    // Xóa đơn hàng khỏi cơ sở dữ liệu
                    boolean isDeleted = orderTableHelper.deleteOrder(currentOrder.getOrderId());

                    if (isDeleted) {
                        orderdetailTableHelper.deleteOrderDetailsByOrderId(currentOrder.getOrderId());
                        // Loại bỏ đơn hàng khỏi danh sách
                        orders.remove(currentPosition);

                        // Cập nhật lại RecyclerView
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, orders.size());

                        Log.d("OrderAdapter", "Deleted order with ID: " + currentOrder.getOrderId());
                    } else {
                        Log.e("OrderAdapter", "Failed to delete order with ID: " + currentOrder.getOrderId());
                    }
                }
            });

        } //else xử lý thanh toán ở đây set lại cho hóa đơn status la true


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
    public interface OnOrderClickListener {
        void onOrderClick(int orderId);  // Callback để lấy orderId
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderDate, tvTotalAmount,tvState;
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
