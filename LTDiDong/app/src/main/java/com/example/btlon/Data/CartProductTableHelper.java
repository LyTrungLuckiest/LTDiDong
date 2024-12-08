package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CartProductTableHelper extends BaseTableHelper<CartProduct> {
    private static final String TABLE_NAME = "CartProduct";
    private static final String COL_CART_PRODUCT_ID = "cart_product_id";
    private static final String COL_CART_ID = "cart_id";
    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_TOTAL_PRICE = "price";

    private ProductTableHelper productTableHelper;

    public CartProductTableHelper(Context context) {
        super(context);
        this.productTableHelper = new ProductTableHelper(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected CartProduct mapCursorToEntity(Cursor cursor) {
        try {
            int cartProductId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_PRODUCT_ID));
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CART_ID));
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));

            // Fetch product using ProductTableHelper
            Product product = productTableHelper.getProductById(productId);

            if (product != null) {

                return new CartProduct( product, quantity);
            } else {
                Log.e("CartProductTableHelper", "Product not found for productId: " + productId);
                return null;
            }
        } catch (Exception e) {
            Log.e("CartProductTableHelper", "Error mapping cursor to CartProduct: " + e.getMessage());
            return null;
        }
    }

    // Add new cart product
    public boolean addOrUpdateCartProduct(int cartId, CartProduct cartProduct) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase(); // Lấy database
        Cursor cursor = null;
        boolean result = false;

        try {
            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
            String selection = COL_CART_ID + " = ? AND " + COL_PRODUCT_ID + " = ?";
            String[] selectionArgs = {String.valueOf(cartId), String.valueOf(cartProduct.getProduct().getId())};

            cursor = db.query(
                    TABLE_NAME,
                    null, // Chọn tất cả các cột
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Nếu tồn tại, lấy số lượng hiện tại và cập nhật
                int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                int newQuantity = currentQuantity + cartProduct.getQuantity();
                double newTotalPrice = Double.parseDouble(cartProduct.getProduct().getPrice()) *newQuantity ; // Cập nhật tổng giá

                ContentValues values = new ContentValues();
                values.put(COL_QUANTITY, newQuantity);
                values.put(COL_TOTAL_PRICE, newTotalPrice);

                // Thực hiện cập nhật
                int rowsUpdated = db.update(
                        TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );

                result = rowsUpdated > 0; // Kiểm tra cập nhật thành công
            } else {
                // Nếu không tồn tại, thêm sản phẩm mới
                ContentValues values = new ContentValues();
                values.put(COL_CART_ID, cartId);
                values.put(COL_PRODUCT_ID, cartProduct.getProduct().getId());
                values.put(COL_QUANTITY, cartProduct.getQuantity());
                values.put(COL_TOTAL_PRICE, cartProduct.getTotalPrice());

                long id = db.insert(TABLE_NAME, null, values);
                result = id != -1; // Kiểm tra thêm mới thành công
            }
        } catch (Exception e) {
            Log.e("CartProductTableHelper", "Error in addOrUpdateCartProduct: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng Cursor để tránh rò rỉ tài nguyên
            }
        }

        return result;
    }


    // Fetch all products in a given cartId
    public List<CartProduct> getCartProductsByCartId(int cartId) {
        List<CartProduct> cartProducts = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = COL_CART_ID + "=?";
            String[] selectionArgs = {String.valueOf(cartId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CartProduct cartProduct = mapCursorToEntity(cursor);
                    if (cartProduct != null) {
                        cartProducts.add(cartProduct);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("CartProductTableHelper", "Error fetching cart products: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("CartProductTableHelper", "Loaded " + cartProducts.size() + " cart products for cartId " + cartId);
        return cartProducts;
    }

    // Delete a cart product by its ID
    public boolean deleteCartProduct(int cartProductId) {
        return delete(COL_CART_PRODUCT_ID + "=?", new String[]{String.valueOf(cartProductId)});
    }
    public void updateCartProductQuantity(int productId, int quantity) {
        // Your code to update the database with the new quantity
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_QUANTITY, quantity);

        // Assuming you have a column `CART_COLUMN_PRODUCT_ID` in your table
        db.update(TABLE_NAME, contentValues, COL_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close();
    }
}
