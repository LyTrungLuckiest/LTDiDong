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
    protected Product mapCursorToEntity(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPTION));
        int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
        int soldQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SOLD_QUANTITY));
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID));

        return new Product(id, name, description, price, image, stock, categoryId, soldQuantity);
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
        String selection = "category_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryId)};
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, COL_CATEGORY_ID, "category_id"},
                selection, selectionArgs, null);
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
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
                    int stock = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                    int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID));

                    products.add(new Product(id, name, description, price, image, stock, categoryId));
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


    public boolean addProduct(Product newProduct) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, newProduct.getName());
        values.put(COL_DESCRIPTION, newProduct.getDescription()); // Thêm mô tả
        values.put(COL_PRICE, newProduct.getPrice());
        values.put(COL_IMAGE, newProduct.getImage()); // Thêm đường dẫn ảnh
        values.put(COL_QUANTITY, newProduct.getQuantity()); // Số lượng trong kho
        values.put(COL_CATEGORY_ID, newProduct.getCategory_id()); // Thêm category_id
        return insert(values);
    }


    public boolean updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_DESCRIPTION, product.getDescription()); // Thêm mô tả
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_IMAGE, product.getImage()); // Thêm đường dẫn ảnh
        values.put(COL_QUANTITY, product.getQuantity()); // Số lượng trong kho
        values.put(COL_CATEGORY_ID, product.getCategory_id()); // Thêm category_id
        return update(values, COL_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

    public String getCategoryName(int categoryId) {
        String categoryName = null;
        String query = "SELECT name FROM Categories WHERE category_id = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                categoryName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error fetching category name", e);
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
            SQLiteDatabase db = getDatabase(); // Lazy initialization
            String selection = COL_ID + " = ?";
            String[] selectionArgs = {String.valueOf(productId)};

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
                return mapCursorToEntity(cursor); // Sử dụng phương thức mapCursorToEntity để chuyển đổi
            }
        } catch (Exception e) {
            Log.e("ProductTableHelper", "Error fetching product by ID: " + e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng cursor để tránh rò rỉ tài nguyên
            }
        }

        return null; // Nếu không tìm thấy sản phẩm


    }

    public boolean updateProductQuantity(int productId, int stockQuantity, int soldQuantity) {
        ContentValues values = new ContentValues();
        values.put("stock_quantity", stockQuantity);
        values.put("sold_quantity", soldQuantity);

        int rowsAffected = db.update("Products", values, "product_id = ?", new String[]{String.valueOf(productId)});
        db.close();

        return rowsAffected > 0;
    }


}
