package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductTableHelper extends BaseTableHelper<Products> {
    private static final String TABLE_NAME = "Products";
    private static final String COL_ID = "product_id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image_url";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_CATEGORY_ID = "category_id";

    public ProductTableHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }


    @Override
    protected Products mapCursorToEntity(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRICE));
        String image = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGE));
        return new Products(name, price, image);
    }

    public boolean addProduct(String name, String price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        return insert(values);
    }

    public List<Products> getAllProducts() {
        return getAll(new String[]{COL_ID, COL_NAME, COL_PRICE, COL_IMAGE}, null, null, null);
    }

    public boolean updateProduct(int productId, String name, String price, String image) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PRICE, price);
        values.put(COL_IMAGE, image);
        return update(values, COL_ID + "=?", new String[]{String.valueOf(productId)});
    }

    public boolean deleteProduct(int productId) {
        return delete(COL_ID + "=?", new String[]{String.valueOf(productId)});
    }


    public List<Products> getNewProducts() {

        String orderBy = COL_CREATED_AT + " DESC";
        String limit = "10";


        return getAll(new String[]{COL_ID, COL_NAME, COL_PRICE, COL_IMAGE, COL_CREATED_AT},
                null, null, orderBy + " LIMIT " + limit);
    }

    public List<Products> getBestSellingProducts() {

        String orderBy = "sold_quantity DESC";
        String limit = "10";


        return getAll(new String[]{COL_ID, COL_NAME, COL_PRICE, COL_IMAGE, COL_CREATED_AT, "sold_quantity"},
                null, null, orderBy + " LIMIT " + limit);
    }
    public List<Products> getProductsByCategory(int categoryId) {
        String selection = COL_CATEGORY_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(categoryId)};

        return getAll(new String[]{COL_ID, COL_NAME, COL_PRICE, COL_IMAGE, COL_CATEGORY_ID},
                selection, selectionArgs, null);
    }
    // Tìm kiếm sản phẩm theo từ khóa
    public List<Products> searchProducts(String query) {
        List<Products> productList = new ArrayList<>();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "
                + COL_NAME + " LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Products product = mapCursorToEntity(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return productList;
    }



}

