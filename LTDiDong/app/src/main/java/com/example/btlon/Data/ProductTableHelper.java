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
        return new Product(id, name, description, price, image, stock);
    }



    public List<Product> getAllProducts() {
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY}, null, null, null);
    }


    public boolean deleteProduct(int productId) {
        return delete(COL_ID + "=?", new String[]{String.valueOf(productId)});
    }

    public List<Product> getNewProducts() {
        String orderBy = "created_at DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, "created_at"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Product> getTopRatedProducts() {
        String orderBy = "rating DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, "rating"},
                null, null, orderBy + " LIMIT " + limit);

    }


    public List<Product> getBestSellingProducts() {
        String orderBy = "sold_quantity DESC";
        String limit = "10";
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, "sold_quantity"},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Product> getProductsByCategory(int categoryId) {
        String selection = "category_id = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryId)};
        return getAll(new String[]{COL_ID, COL_NAME, COL_DESCRIPTION, COL_PRICE, COL_IMAGE, COL_QUANTITY, "category_id"},
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

                    products.add(new Product(id, name, description, price, image, stock));
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
        return insert(values);
    }


    public boolean updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, product.getName());
        values.put(COL_DESCRIPTION, product.getDescription()); // Thêm mô tả
        values.put(COL_PRICE, product.getPrice());
        values.put(COL_IMAGE, product.getImage()); // Thêm đường dẫn ảnh
        values.put(COL_QUANTITY, product.getQuantity()); // Số lượng trong kho
        return update(values, COL_ID + "=?", new String[]{String.valueOf(product.getId())});
    }

}
