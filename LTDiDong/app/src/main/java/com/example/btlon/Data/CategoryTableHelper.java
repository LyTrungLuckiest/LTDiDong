package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoryTableHelper {

    private SQLiteDatabase db;
    private SqliteHelper dbHelper;

    public CategoryTableHelper(Context context) {
        dbHelper = new SqliteHelper(context);
    }

    // Add a new category
    public boolean addCategory(String category) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category);

        long result = db.insert("Categories", null, values);
        db.close();
        return result != -1;
    }

    // Update an existing category using Category object
    public boolean updateCategory(Category category) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", category.getName());

        int rowsAffected = db.update("Categories", values, "category_id = ?", new String[]{String.valueOf(category.getCategory_id())});
        db.close();
        return rowsAffected > 0;
    }

    // Update an existing category by ID and name
    public boolean updateCategory(int categoryId, String updatedCategoryName) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", updatedCategoryName);

        int rowsAffected = db.update("Categories", values, "category_id = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsAffected > 0;
    }

    // Delete a category
    public boolean deleteCategory(int categoryId) {
        db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("Categories", "category_id = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsAffected > 0;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        db = dbHelper.getReadableDatabase();
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = db.query("Categories", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                Category category = new Category(id, name);
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return categoryList;
    }
}
