package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ProductTableHelper extends BaseTableHelper<Product> {
    private static final String TABLE_NAME = "Products";
    private static final String COL_ID = "product_id";
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image_url";
    private static final String COL_QUANTITY = "stock_quantity";
    private static final String COL_CATEGORY_ID = "category_id";
    private static final String COL_SOLD_QUANTITY = "sold_quantity";

    private SQLiteDatabase database;
    private final Context context;

    public ProductTableHelper(Context context) {
        super(context);
        this.context = context;
    }

    // Lazy initialization for SQLiteDatabase
    private SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            SqliteHelper dbHelper = new SqliteHelper(context);
            database = dbHelper.getWritableDatabase(); // Use writable database
        }
        return database;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Product mapCursorToEntity(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
        int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID));

        return new Product(id, name, description, price, image, stock, categoryId);
    }

    public List<Product> getAllProducts() {
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, COL_CATEGORY_ID}, null, null, null);
    }

    public boolean deleteProduct(int productId) {
        return delete(COL_ID + "=?", new String[]{String.valueOf(productId)});
    }

    public List<Product> getNewProducts() {
        String orderBy = "created_at DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, COL_CATEGORY_ID, "created_at"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Product> getTopRatedProducts() {
        String orderBy = "rating DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, COL_CATEGORY_ID, "rating"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Product> getBestSellingProducts() {
        String orderBy = "sold_quantity DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, COL_CATEGORY_ID, "sold_quantity"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        String selection = COL_CATEGORY_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryId)};
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, COL_CATEGORY_ID},
                selection, selectionArgs, null);
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getDatabase();
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_NAME + " LIKE ? OR " + COL_DESCRIPTION + " LIKE ?";
            String searchKeyword = "%" + keyword + "%";
            cursor = db.rawQuery(query, new String[]{searchKeyword, searchKeyword});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    products.add(mapCursorToEntity(cursor));
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("ProductTableHelper", "Error searching products: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return products;
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public boolean addProduct(Product newProduct) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newProduct.getName());
        values.put(COL_DESCRIPTION, newProduct.getDescription());
        values.put(COL_PRICE, newProduct.getPrice());
        values.put(COL_IMAGE, newProduct.getImage());
        values.put(COL_QUANTITY, newProduct.getQuantity());
        values.put(COL_CATEGORY_ID, newProduct.getCategory_id());
        return insert(values);
    }

    public boolean updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_DESCRIPTION, product.getDescription());
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_IMAGE, product.getImage());
        values.put(COL_QUANTITY, product.getQuantity());
        values.put(COL_CATEGORY_ID, product.getCategory_id());
        return update(values, COL_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

    public String getCategoryName(int categoryId) {
        String categoryName = null;
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getDatabase();
            String query = "SELECT name FROM Categories WHERE category_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});

            if (cursor != null && cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
        } catch (Exception e) {
            Log.e("ProductTableHelper", "Error fetching category name: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return categoryName;
    }

    public Product getProductById(int productId) {
        Cursor cursor = null;

        try {
            SQLiteDatabase db = getDatabase();
            String selection = COL_ID + " = ?";
            String[] selectionArgs = {String.valueOf(productId)};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return mapCursorToEntity(cursor);
            }
        } catch (Exception e) {
            Log.e("ProductTableHelper", "Error fetching product by ID: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;
    }

    public boolean updateProductQuantity(int productId, int quantitySold) {
        // Fetch the current product details to calculate the new stock and sold quantities
        Product product = getProductById(productId);
        if (product != null) {
            int currentStock = product.getQuantity();
            int currentSold = product.getSoldQuantity();

            // Calculate the new values
            int newStock = currentStock - quantitySold;  // Decrease stock by the sold quantity
            int newSold = currentSold + quantitySold;    // Increase sold quantity

            // Ensure stock does not go negative
            if (newStock < 0) {
                Log.e("ProductTableHelper", "Stock cannot be negative for product ID: " + productId);
                return false;
            }

            // Update the product in the database
            ContentValues values = new ContentValues();
            values.put(COL_QUANTITY, newStock);
            values.put(COL_SOLD_QUANTITY, newSold);

            SQLiteDatabase db = getDatabase();
            int rowsAffected = db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(productId)});

            return rowsAffected > 0;
        } else {
            Log.e("ProductTableHelper", "Product not found for ID: " + productId);
            return false;
        }
    }

}
