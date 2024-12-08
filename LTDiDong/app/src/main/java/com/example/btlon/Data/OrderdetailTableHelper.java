package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class OrderdetailTableHelper extends BaseTableHelper<OrderDetail> {
    private static final String TABLE_NAME = "OrderDetails";
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

        Log.d("OrderDetailTableHelper", "Adding order detail: " + orderDetail);
        return insert(values);
    }

    // Fetch all order details for a given orderId
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
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
}
