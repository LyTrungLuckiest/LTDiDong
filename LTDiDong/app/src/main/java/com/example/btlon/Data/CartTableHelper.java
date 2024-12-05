package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartTableHelper {

    private SQLiteDatabase db;

    public CartTableHelper(Context context) {
        db = new SqliteHelper(context).getWritableDatabase();
    }

    // Kiểm tra xem sản phẩm có tồn tại trong giỏ hàng của người dùng hay không
    public boolean isExist(int userId, int productId) {
        String selection = "user_id = ? AND product_id = ?";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(productId)};
        Cursor cursor = null;
        boolean isExist = false;

        try {
            cursor = db.query("Cart", null, selection, selectionArgs, null, null, null);
            isExist = cursor.getCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return isExist;
    }

    // Thêm sản phẩm vào giỏ hàng hoặc cập nhật số lượng nếu sản phẩm đã tồn tại
    public boolean addItemToCart(int userId, int productId, int quantity) {
        if (isExist(userId, productId)) {
            // Lấy cart_id của sản phẩm đã tồn tại
            int cartId = getCartId(userId, productId);
            if (cartId != -1) {
                // Cập nhật số lượng sản phẩm
                int currentQuantity = getProductQuantity(cartId);
                int newQuantity = currentQuantity + quantity;
                return updateQuantity(cartId, newQuantity);
            }
            return false;  // Trường hợp không lấy được cart_id
        } else {
            // Thêm sản phẩm vào giỏ hàng nếu chưa tồn tại
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("product_id", productId);
            values.put("quantity", quantity);

            long newRowId = db.insert("Cart", null, values);
            return newRowId != -1;  // Trả về true nếu thêm thành công
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public boolean updateQuantity(int cartId, int newQuantity) {
        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);

        int rowsAffected = db.update("Cart", values, "cart_id = ?", new String[]{String.valueOf(cartId)});
        return rowsAffected > 0;
    }

    // Lấy cart_id của sản phẩm dựa trên userId và productId
    private int getCartId(int userId, int productId) {
        String selection = "user_id = ? AND product_id = ?";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(productId)};
        Cursor cursor = null;
        int cartId = -1;

        try {
            cursor = db.query("Cart", new String[]{"cart_id"}, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                cartId = cursor.getInt(cursor.getColumnIndexOrThrow("cart_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return cartId;
    }

    // Lấy số lượng của sản phẩm trong giỏ hàng
    private int getProductQuantity(int cartId) {
        String selection = "cart_id = ?";
        String[] selectionArgs = {String.valueOf(cartId)};
        Cursor cursor = null;
        int quantity = 0;

        try {
            cursor = db.query("Cart", new String[]{"quantity"}, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return quantity;
    }

    // Lấy tất cả sản phẩm trong giỏ hàng của người dùng
    public List<Cart> getCartItems(int userId) {
        List<Cart> cartList = new ArrayList<>();
        String selection = "user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        Cursor cursor = db.query("Cart", null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("cart_id"));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow("product_id"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

            Product product = getProductById(productId);

            if (product != null) {
                Cart cart = new Cart(cartId, userId, productId, quantity, product.getName(), product.getPrice(), product.getImage());
                cartList.add(cart);
            }
        }

        cursor.close();
        return cartList;
    }

    // Lấy thông tin sản phẩm từ cơ sở dữ liệu theo product_id
    private Product getProductById(int productId) {
        Cursor cursor = db.query("Products", null, "product_id = ?", new String[]{String.valueOf(productId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url"));

            Product product = new Product(productId, name, price, imageUrl);
            cursor.close();
            return product;
        }

        cursor.close();
        return null;
    }

    // Xóa tất cả sản phẩm trong giỏ hàng của người dùng
    public void clearCart(int userId) {
        String whereClause = "user_id = ?";
        String[] whereArgs = {String.valueOf(userId)};
        db.delete("Cart", whereClause, whereArgs);
    }

    // Xóa sản phẩm trong giỏ hàng theo cart_id
    public void removeItemFromCart(int cartId) {
        db.delete("Cart", "cart_id = ?", new String[]{String.valueOf(cartId)});
    }

    // Đóng kết nối cơ sở dữ liệu
    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
