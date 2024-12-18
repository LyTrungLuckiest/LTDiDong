package com.example.btlon.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OrderdetailTableHelper extends BaseTableHelper<OrderDetail> {
    private static final String TABLE_NAME = "Order_Details";
    private static final String COL_ORDER_DETAIL_ID = "order_detail_id";
    private static final String COL_ORDER_ID = "order_id";
    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_QUANTITY = "quantity";

    private ProductTableHelper productTableHelper;

    public OrderdetailTableHelper(Context context) {
        super(context);
        this.productTableHelper = new ProductTableHelper(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected OrderDetail mapCursorToEntity(Cursor cursor) {
        try {
            int orderDetailId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ORDER_DETAIL_ID));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));

            // Fetch product using ProductTableHelper
            Product product = productTableHelper.getProductById(productId);

            if (product != null) {
                return new OrderDetail(orderDetailId, product, quantity);
            } else {
                Log.e("OrderDetailTableHelper", "Product not found for productId: " + productId);
                return null;
            }
        } catch (Exception e) {
            Log.e("OrderDetailTableHelper", "Error mapping cursor to OrderDetail: " + e.getMessage());
            return null;
        }
    }

    // Add a new order detail
    public boolean addOrderDetail(int orderId, OrderDetail orderDetail) {
        ContentValues values = new ContentValues();
        values.put(COL_ORDER_ID, orderId);
        values.put(COL_PRODUCT_ID, orderDetail.getProduct().getId());
        values.put(COL_QUANTITY, orderDetail.getQuantity());

        // Make sure to include the price for the product in the order detail
        double price = Double.parseDouble(orderDetail.getProduct().getPrice());  // Assuming getPrice() method exists in the Product class
        values.put("price", price);  // Adding price to the values

        Log.d("OrderDetailTableHelper", "Adding order detail: " + orderDetail);
        return insert(values);
    }


    // Fetch all order details for a given orderId
    public List<OrderDetail> getListOrderDetailsByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = COL_ORDER_ID + "=?";
            String[] selectionArgs = {String.valueOf(orderId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    OrderDetail orderDetail = mapCursorToEntity(cursor);
                    if (orderDetail != null) {
                        orderDetails.add(orderDetail);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("OrderDetailTableHelper", "Error fetching order details: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("OrderDetailTableHelper", "Loaded " + orderDetails.size() + " order details for orderId " + orderId);
        return orderDetails;
    }

    // Delete an order detail by its ID
    public boolean deleteOrderDetail(int orderDetailId) {
        return delete(COL_ORDER_DETAIL_ID + "=?", new String[]{String.valueOf(orderDetailId)});
    }
    public Cursor getOrderDetailsByOrderId(int orderId) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        // Modify this SQL query to fetch details for a specific orderId
        String query = "SELECT * FROM Order_Details WHERE order_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderId)});
    }
    public boolean deleteOrderDetailsByUserId(int userId) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        try {
            db.execSQL("PRAGMA foreign_keys = ON;");
            // Truy vấn SQL để xóa order details dựa trên user_id
            String query = "DELETE FROM Order_Details " +
                    "WHERE order_id IN (SELECT order_id FROM Orders WHERE user_id = ?)";
            db.execSQL(query, new Object[]{userId});
            Log.d("OrderDetailTableHelper", "Deleted order details for user_id: " + userId);
            return true;
        } catch (Exception e) {
            Log.e("OrderDetailTableHelper", "Error deleting order details for user_id: " + userId, e);
            return false;
        }
    }

    // Xóa tất cả các OrderDetail
    public boolean deleteAllOrderDetails() {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        try {
            // Xóa tất cả bản ghi trong bảng Order_Details
            int rowsDeleted = db.delete(TABLE_NAME, null, null);

            if (rowsDeleted > 0) {
                Log.d("OrderDetailTableHelper", "Đã xóa " + rowsDeleted + " chi tiết đơn hàng.");
                return true;
            } else {
                Log.d("OrderDetailTableHelper", "Không có chi tiết đơn hàng nào để xóa.");
                return false;
            }
        } catch (Exception e) {
            Log.e("OrderDetailTableHelper", "Lỗi khi xóa tất cả OrderDetails: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }
    public boolean deleteOrderDetailsByOrderId(int orderId) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();

        try {
            // Truy vấn SQL để xóa tất cả order details theo order_id
            int rowsDeleted = db.delete(TABLE_NAME, COL_ORDER_ID + " = ?", new String[]{String.valueOf(orderId)});

            if (rowsDeleted > 0) {
                Log.d("OrderDetailTableHelper", "Đã xóa " + rowsDeleted + " chi tiết đơn hàng với order_id: " + orderId);
                return true;
            } else {
                Log.d("OrderDetailTableHelper", "Không có chi tiết đơn hàng nào để xóa với order_id: " + orderId);
                return false;
            }
        } catch (Exception e) {
            Log.e("OrderDetailTableHelper", "Lỗi khi xóa order details cho order_id: " + orderId, e);
            return false;
        } finally {
            db.close();
        }
    }





}
