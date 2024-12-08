package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OrderTableHelper extends BaseTableHelper<Order> {
    private static final String TABLE_NAME = "Orders";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_ORDER_DATE = "order_date";
    private static final String COL_TOTAL_AMOUNT = "total_amount";
    private static final String COL_STATUS = "status";
    private static final String COL_ORDER_DETAILS = "order_details";

    private SqliteHelper sqliteHelper;

    public OrderTableHelper(Context context) {
        super(context);
        this.sqliteHelper = new SqliteHelper(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Order mapCursorToEntity(Cursor cursor) {
        try {
            int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_ORDER_DATE));
            double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_AMOUNT));
            boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STATUS)) == 0;

            // You can implement fetching order details if needed
            List<OrderDetail> orderDetails = new ArrayList<>(); // Placeholder for actual implementation

            return new Order(orderId, new Users(userId), orderDate, totalAmount, orderDetails ,status );
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error mapping cursor to Order entity: " + e.getMessage());
            return null;
        }
    }

    // Thêm mới đơn hàng
    public boolean addOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, order.getUser().getUserId());
        values.put(COL_ORDER_DATE, order.getOrderDate());
        values.put(COL_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COL_STATUS, order.isStatus() ? 1 : 0);

        // Log thông tin
        Log.d("OrderTableHelper", "Thêm đơn hàng: " + order);

        return insert(values);
    }

    // Cập nhật trạng thái thanh toán
    public boolean updateOrderStatus(int orderId, boolean status) {
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, status ? 1 : 0);

        return update(values, COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    // Xóa đơn hàng
    public boolean deleteOrder(int orderId) {
        return delete(COL_ORDER_ID + "=?", new String[]{String.valueOf(orderId)});
    }

    // Lấy tất cả đơn hàng của người dùng
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = COL_USER_ID + "=?";
            String[] selectionArgs = {String.valueOf(userId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Order order = mapCursorToEntity(cursor);
                    if (order != null) {
                        orders.add(order);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error fetching orders: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("OrderTableHelper", "Loaded " + orders.size() + " orders for userId " + userId);
        return orders;
    }
}
