package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.btlon.OrderStatistic.CategoryOrderCount;
import com.example.btlon.OrderStatistic.DateOrderCount;
import com.example.btlon.OrderStatistic.UserOrderCount;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderTableHelper extends BaseTableHelper<Order> {
    private static final String TABLE_NAME = "Orders";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_ORDER_DATE = "order_date";
    private static final String COL_TOTAL_AMOUNT = "total_amount";
    private static final String COL_STATUS = "status";
    private static final String COL_ORDER_DETAILS = "order_details";

    private SqliteHelper sqliteHelper;
    Context context;


    public OrderTableHelper(Context context) {
        super(context);
        this.context=context;
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
        values.put(COL_ORDER_DATE, new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

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

    public List<Order> getAllOrdersWithDetails() {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = null;
        ProductTableHelper productTableHelper = new ProductTableHelper(context);
        try {
            // Truy vấn để lấy toàn bộ đơn hàng kèm chi tiết
            String query = "SELECT o.order_id, o.user_id, o.order_date, o.total_amount, o.status, " +
                    "od.order_detail_id AS detail_id, od.product_id, od.quantity, od.price " +
                    "FROM Orders o " +
                    "LEFT JOIN Order_Details od ON o.order_id = od.order_id " +
                    "ORDER BY o.order_id";

            cursor = db.rawQuery(query, null);
            Log.d("OrderTableHelper", "Query executed: " + query);

            // Map kết quả vào danh sách `Order`
            int currentOrderId = -1;
            Order currentOrder = null;
            List<OrderDetail> orderDetails = null;

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("order_id"));
                    if (orderId != currentOrderId) {
                        // Thêm đơn hàng trước đó vào danh sách (nếu có)
                        if (currentOrder != null) {
                            currentOrder.setOrderDetails(orderDetails);
                            orders.add(currentOrder);
                            Log.d("OrderTableHelper", "Order added: " + currentOrder.toString());
                        }

                        // Tạo đối tượng `Order` mới
                        int userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
                        String orderDate = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                        double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("total_amount"));
                        boolean status = cursor.getInt(cursor.getColumnIndexOrThrow("status")) == 1;

                        currentOrder = new Order(orderId, new Users(userId), orderDate, totalAmount, new ArrayList<>(), status);
                        currentOrderId = orderId;
                        orderDetails = new ArrayList<>();
                        Log.d("OrderTableHelper", "New order created: " + currentOrder.toString());
                    }

                    // Kiểm tra và thêm chi tiết đơn hàng (nếu có)
                    int detailIdIndex = cursor.getColumnIndex("detail_id");
                    if (detailIdIndex >= 0 && !cursor.isNull(detailIdIndex)) {
                        int detailId = cursor.getInt(detailIdIndex);
                        int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
                        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

                        Log.d("OrderTableHelper", "Detail found - ID: " + detailId + ", ProductID: " + productId + ", Quantity: " + quantity);

                        // Lấy thông tin sản phẩm
                        Product product = productTableHelper.getProductById(productId);
                        OrderDetail orderDetail = new OrderDetail(detailId, product, quantity);
                        orderDetails.add(orderDetail);
                        Log.d("OrderTableHelper", "OrderDetail added: " + orderDetail.toString());
                    }
                } while (cursor.moveToNext());
            }

            // Thêm đơn hàng cuối cùng vào danh sách
            if (currentOrder != null) {
                currentOrder.setOrderDetails(orderDetails);
                orders.add(currentOrder);
                Log.d("OrderTableHelper", "Final order added: " + currentOrder.toString());
            }
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error fetching orders with details: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return orders;
    }



    public int getUserIdByOrderId(int orderId) {
        Cursor cursor = null;
        int userId = -1;  // Default value to indicate no result
        try {
            // Query the Orders table with the given orderId
            String selection = COL_ORDER_ID + "=?";
            String[] selectionArgs = {String.valueOf(orderId)};
            cursor = db.query(TABLE_NAME, new String[]{COL_USER_ID}, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve userId from the cursor
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            }
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error fetching userId for orderId " + orderId + ": " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return userId;  // Return the userId (or -1 if not found)
    }

    public List<UserOrderCount> getUserOrderCounts() {
        List<UserOrderCount> userOrderCounts = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Viết câu lệnh SQL GROUP BY
            String query = "SELECT " + COL_USER_ID + ", COUNT(" + COL_ORDER_ID + ") AS total_orders " +
                    "FROM " + TABLE_NAME + " " +
                    "GROUP BY " + COL_USER_ID;

            // Thực thi câu lệnh
            cursor = db.rawQuery(query, null);

            // Duyệt kết quả trả về
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
                    int totalOrders = cursor.getInt(cursor.getColumnIndexOrThrow("total_orders"));

                    // Thêm vào danh sách kết quả
                    userOrderCounts.add(new UserOrderCount(userId, totalOrders));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error getting user order counts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng Cursor
            }
        }

        return userOrderCounts; // Trả về danh sách kết quả
    }
    public List<DateOrderCount> getOrdersGroupedByDate() {
        List<DateOrderCount> dateOrderCountList = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Truy vấn SQL để lấy số lượng đơn hàng theo ngày
            String query = "SELECT order_date, COUNT(order_id) AS total_orders " +
                    "FROM Orders " +
                    "GROUP BY order_date";
            cursor = db.rawQuery(query, null);

            Log.d("SQL Query", "Executing: " + query);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Lấy giá trị từ cursor
                    String orderDate = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                    int totalOrders = cursor.getInt(cursor.getColumnIndexOrThrow("total_orders"));

                    // Ghi log từng dòng dữ liệu
                    Log.d("Cursor Data", "order_date: " + orderDate + ", total_orders: " + totalOrders);

                    // Tạo đối tượng và thêm vào danh sách
                    dateOrderCountList.add(new DateOrderCount(orderDate, totalOrders));
                } while (cursor.moveToNext());
            } else {
                Log.d("Cursor Data", "No data found.");
            }
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error fetching order statistics: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return dateOrderCountList;
    }

    public List<CategoryOrderCount> getOrdersGroupedByCategory() {
        List<CategoryOrderCount> categoryOrderCountList = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Truy vấn SQL để lấy số lượng đơn hàng theo category_id
            String query = "SELECT p.category_id, COUNT(od.order_id) AS total_orders " +
                    "FROM Order_Details od " +
                    "JOIN Products p ON od.product_id = p.product_id " +
                    "GROUP BY p.category_id";

            // Thực thi truy vấn
            cursor = db.rawQuery(query, null);

            // Kiểm tra và xử lý kết quả
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Lấy dữ liệu từ cursor
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                    int totalOrders = cursor.getInt(cursor.getColumnIndexOrThrow("total_orders"));

                    Log.d("Cursor Data", "categoryId: " + categoryId + ", totalOrders: " + totalOrders);

                    // Tạo đối tượng CategoryOrderCount và thêm vào danh sách
                    categoryOrderCountList.add(new CategoryOrderCount(categoryId, totalOrders));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("OrderTableHelper", "Error fetching order statistics by category: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryOrderCountList;
    }




}
