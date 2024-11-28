package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProductTableHelper extends BaseTableHelper<Products> {
    private static final String TABLE_NAME = "Products";
    private static final String COL_ID = "product_id";
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image_url";
    private static final String COL_QUANTITY = "quantity";

    private SQLiteDatabase database;
    private final Context context;

    public ProductTableHelper(Context context) {
        super(context);
        this.context = context;
    }

    // Lazy initialization cho SQLiteDatabase
    private SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            SqliteHelper dbHelper = new SqliteHelper(context);
            database = dbHelper.getReadableDatabase();
        }
        return database;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Products mapCursorToEntity(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
        return new Products(id, name,description, price, image);
    }

    public boolean addProduct(String name,String description, String price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        return insert(values);
    }

    public List<Products> getAllProducts() {
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE}, null, null, null);
    }

    public boolean updateProduct(int productId, String name,String description, String price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_DESCRIPTION,description);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        return update(values, COL_ID + "=?", new String[]{String.valueOf(productId)});
    }

    public boolean deleteProduct(int productId) {
        return delete(COL_ID + "=?", new String[]{String.valueOf(productId)});
    }

    public List<Products> getNewProducts() {
        String orderBy = "created_at DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME,COL_DESCRIPTION, COL_PRICE, COL_IMAGE, "created_at"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Products> getBestSellingProducts() {
        String orderBy = "sold_quantity DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, "sold_quantity"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Products> getProductsByCategory(int categoryId) {
        String selection = "category_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryId)};
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, "category_id"},
                selection, selectionArgs, null);
    }

    public List<Products> searchProducts(String keyword) {
        List<Products> products = new ArrayList<>();
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getDatabase(); // Sử dụng lazy initialization
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_NAME + " LIKE ? OR " + COL_DESCRIPTION + " LIKE ?";
            String searchKeyword = "%" + keyword + "%"; // Tìm kiếm kiểu 'contains'
            cursor = db.rawQuery(query, new String[]{searchKeyword, searchKeyword});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));

                    products.add(new Products(id, name,description, price, image));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("ProductTableHelper", "Lỗi khi tìm kiếm sản phẩm: " + e.getMessage());
            e.printStackTrace(); // Ghi log chi tiết để dễ debug
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng cursor
            }
        }

        return products; // Nếu không có sản phẩm, trả về danh sách trống
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public boolean addProduct(String productName, double price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, productName);
        values.put(COL_PRICE, price);
        values.put(COL_QUANTITY, quantity);
        return insert(values);
    }

    public boolean updateProduct(int id, String productName, double price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, productName);
        values.put(COL_PRICE, price);
        values.put(COL_QUANTITY, quantity);
        return update(values, COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean addProduct(Products newProduct) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newProduct.getName());
        values.put(COL_PRICE, newProduct.getPrice());
        values.put(COL_QUANTITY, newProduct.getQuality());
        return insert(values);
    }

    public boolean updateProduct(Products product) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_QUANTITY, product.getQuality());
        return update(values, COL_ID + "=?", new String[]{String.valueOf(product.getId())});
    }
}
