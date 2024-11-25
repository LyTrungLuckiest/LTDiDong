package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

public class CategoryTableHelper extends BaseTableHelper<Category> {

    private static final String TABLE_NAME = "Categories";
    private static final String COL_ID = "category_id";
    private static final String COL_NAME = "name";

    public CategoryTableHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Category mapCursorToEntity(Cursor cursor) {

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        return new Category(id, name);
    }


    public boolean addCategory(String name) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        return insert(values);
    }


    public List<Category> getAllCategories() {
        return getAll(new String[]{COL_ID, COL_NAME}, null, null, null); // Lấy tất cả danh mục
    }


    public boolean updateCategory(int categoryId, String name) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        return update(values, COL_ID + "=?", new String[]{String.valueOf(categoryId)});
    }


    public boolean deleteCategory(int categoryId) {
        return delete(COL_ID + "=?", new String[]{String.valueOf(categoryId)});
    }

}
