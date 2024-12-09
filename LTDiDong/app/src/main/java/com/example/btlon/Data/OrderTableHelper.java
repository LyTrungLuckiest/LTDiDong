package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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


    // Phương thức thêm đơn hàng vào cơ sở dữ liệu
    public int addOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, order.getUser().getUserId());
        values.put(COL_ORDER_DATE, order.getOrderDate());
        values.put(COL_TOTAL_AMOUNT, order.getTotalAmount());
        values.put(COL_STATUS, order.isStatus() ? 1 : 0);
        Log.d("DEBUG", "User ID: " + order.getUser().getUserId());


        // Gán các giá trị khác cho đơn hàng (ví dụ: thời gian tạo, trạng thái)

        long orderId = db.insert(TABLE_NAME, null, values); // Thêm vào bảng Order và lấy ID
        return (int) orderId; // Chuyển từ long sang int, lưu ý rằng nếu ID quá lớn có thể bị tràn
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
    public Cursor getOrdersForUser(int userId) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        String selection = "user_id=?";
        String[] selectionArgs = {String.valueOf(userId)};
        return db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    // Phương thức xóa toàn bộ đơn hàng trong bảng Orders
    public boolean deleteAllOrders() {
        try {
            int rowsDeleted = db.delete(TABLE_NAME, null, null);
            Log.d("OrderTableHelper", "Deleted " + rowsDeleted + " orders.");
            return rowsDeleted > 0; // Trả về true nếu có ít nhất một hàng bị xóa
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error deleting all orders: " + e.getMessage());
            return false;
        }
    }
    public boolean deleteAllOrdersByUserId(int userId) {
        try {
            // Truy vấn xóa tất cả các đơn hàng có user_id tương ứng
            String whereClause = "user_id = ?";
            String[] whereArgs = {String.valueOf(userId)};

            int rowsDeleted = db.delete(TABLE_NAME, whereClause, whereArgs);
            Log.d("OrderTableHelper", "Deleted " + rowsDeleted + " orders for user_id: " + userId);

            return rowsDeleted > 0; // Trả về true nếu có ít nhất một hàng bị xóa
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error deleting orders for user_id: " + userId, e);
            return false;
        }
    }

    public void logCursorData(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Get the index for each column and check if it's valid
                int orderIdIndex = cursor.getColumnIndex("order_id");
                int userIdIndex = cursor.getColumnIndex("user_id");
                int totalAmountIndex = cursor.getColumnIndex("total_amount");
                int statusIndex = cursor.getColumnIndex("status");
                int orderDateIndex = cursor.getColumnIndex("order_date");

                // Check if the indices are valid (greater than or equal to 0)
                if (orderIdIndex >= 0 && userIdIndex >= 0 && totalAmountIndex >= 0 && statusIndex >= 0 && orderDateIndex >= 0) {
                    // Extract values from the cursor
                    int orderId = cursor.getInt(orderIdIndex);
                    int userId = cursor.getInt(userIdIndex);
                    double totalAmount = cursor.getDouble(totalAmountIndex);
                    String status = cursor.getString(statusIndex);
                    String orderDate = cursor.getString(orderDateIndex);

                    // Log the extracted data
                    Log.d("CursorData", "Order ID: " + orderId);
                    Log.d("CursorData", "User ID: " + userId);
                    Log.d("CursorData", "Total Amount: " + totalAmount);
                    Log.d("CursorData", "Status: " + status);
                    Log.d("CursorData", "Order Date: " + orderDate);
                } else {
                    // If any of the columns are missing, log an error message
                    Log.e("CursorData", "One or more columns are missing.");
                }
            } while (cursor.moveToNext());
            cursor.close();  // Don't forget to close the cursor after usage
        } else {
            Log.e("CursorData", "No data found or cursor is null.");
        }
    }



}
