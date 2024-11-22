package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

public class ProductTableHelper extends BaseTableHelper<Products> {
    private static final String TABLE_NAME = "Products";
    private static final String COL_ID = "product_id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_IMAGE = "image_url";

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
}

