package com.example.btlon.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CartTableHelper extends BaseTableHelper<Cart> {
    private static final String TABLE_NAME = "Cart";
    private static final String COL_CART_ID = "cart_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_CART_DATE = "cart_date";
    private static final String COL_TOTAL_AMOUNT = "total_amount";
    private static final String COL_STATUS = "status";

    private SqliteHelper sqliteHelper;

    public CartTableHelper(Context context) {
        super(context);
        this.sqliteHelper = new SqliteHelper(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Cart mapCursorToEntity(Cursor cursor) {
        try {
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_ID));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
            String cartDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_CART_DATE));
            double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_TOTAL_AMOUNT));
            boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STATUS)) == 1;

            // Fetch cart products (this can be done separately)
            List<CartProduct> cartProducts = new ArrayList<>(); // Placeholder for fetching cart products

            return new Cart(cartId, new Users(userId), cartDate, totalAmount, cartProducts, status);
        } catch (Exception e) {
            Log.e("CartTableHelper", "Error mapping cursor to Cart entity: " + e.getMessage());
            return null;
        }
    }

    // Add new cart
    public boolean addCart(Cart cart, int userId) {
        ContentValues values = new ContentValues();
        values.put(COL_CART_ID, userId);  // Sử dụng userId làm cartId
        values.put(COL_USER_ID, userId);
        values.put(COL_TOTAL_AMOUNT, cart.getTotalAmount());
        values.put(COL_STATUS, cart.isStatus() ? 1 : 0);

        Log.d("CartTableHelper", "Adding cart for userId: " + userId);
        return insert(values);  // Thêm giỏ hàng vào cơ sở dữ liệu
    }


    // Update cart status (paid/unpaid)
    public boolean updateCartStatus(int cartId, boolean status) {
        ContentValues values = new ContentValues();
        values.put(COL_STATUS, status ? 1 : 0);
        return update(values, COL_CART_ID + "=?", new String[]{String.valueOf(cartId)});
    }

    // Delete cart
    public boolean deleteCart(int cartId) {
        return delete(COL_CART_ID + "=?", new String[]{String.valueOf(cartId)});
    }

    // Fetch all carts for a user
    public List<Cart> getCartsByUserId(int userId) {
        List<Cart> carts = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = COL_USER_ID + "=?";
            String[] selectionArgs = {String.valueOf(userId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Cart cart = mapCursorToEntity(cursor);
                    if (cart != null) {
                        carts.add(cart);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("CartTableHelper", "Error fetching carts: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("CartTableHelper", "Loaded " + carts.size() + " carts for userId " + userId);
        return carts;
    }
    // Xóa tất cả các CartProduct liên quan đến cartId
    public boolean deleteCartProductsByCartId(int cartId) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase(); // Lấy cơ sở dữ liệu
        boolean result = false;

        try {
            // Xóa tất cả sản phẩm trong giỏ hàng
            int rowsDeleted = db.delete(
                    "CartProduct", // Tên bảng chứa các sản phẩm trong giỏ
                    COL_CART_ID + "=?", // Điều kiện lọc
                    new String[]{String.valueOf(cartId)} // Tham số điều kiện lọc
            );

            result = rowsDeleted > 0; // Kiểm tra số dòng đã bị xóa
            Log.d("CartTableHelper", "Deleted " + rowsDeleted + " CartProducts for cartId " + cartId);
        } catch (Exception e) {
            Log.e("CartTableHelper", "Error deleting cart products: " + e.getMessage(), e);
        }
        finally {
            db.close(); // Đảm bảo đóng kết nối cơ sở dữ liệu sau khi thao tác hoàn tất
        }

        return result;
    }
    public boolean deleteCartProductByProductId(int productId) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase(); // Get the writable database
        boolean result = false;

        try {
            // Delete CartProduct by productId
            int rowsDeleted = db.delete(
                    "CartProduct",  // Table name
                    "product_id=?", // Condition to match the productId
                    new String[]{String.valueOf(productId)} // The productId to delete
            );

            result = rowsDeleted > 0; // Check if any rows were deleted
            Log.d("CartTableHelper", "Deleted " + rowsDeleted + " CartProducts for productId " + productId);
        } catch (Exception e) {
            Log.e("CartTableHelper", "Error deleting cart product by productId: " + e.getMessage(), e);
        } finally {
            db.close(); // Ensure the database connection is closed after the operation
        }

        return result;
    }



}
