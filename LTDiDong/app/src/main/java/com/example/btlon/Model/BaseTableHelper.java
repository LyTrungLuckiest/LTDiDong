package com.example.btlon.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTableHelper<T> {
    protected SQLiteDatabase db;
    protected SqliteHelper sqliteHelper;

    public BaseTableHelper(Context context) {
        sqliteHelper = new SqliteHelper(context);
        db = sqliteHelper.getWritableDatabase();
    }

    private boolean isTableExists() {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{getTableName()});
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public boolean insert(ContentValues values) {
        if (!isTableExists()) {
            return false;
        }
        long result = db.insert(getTableName(), null, values);
        return result != -1;
    }

    public boolean update(ContentValues values, String whereClause, String[] whereArgs) {
        if (!isTableExists()) {
            return false;
        }
        int result = db.update(getTableName(), values, whereClause, whereArgs);
        return result > 0;
    }

    public boolean delete(String whereClause, String[] whereArgs) {
        if (!isTableExists()) {
            return false;
        }
        int result = db.delete(getTableName(), whereClause, whereArgs);
        return result > 0;
    }

    public List<T> getAll(String[] columns, String selection, String[] selectionArgs, String orderBy) {
        if (!isTableExists()) {
            return new ArrayList<>();
        }
        List<T> list = new ArrayList<>();
        Cursor cursor = db.query(getTableName(), columns, selection, selectionArgs, null, null, orderBy);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(mapCursorToEntity(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public T getOne(String[] columns, String selection, String[] selectionArgs) {
        if (!isTableExists()) {
            return null;
        }
        T entity = null;
        Cursor cursor = db.query(getTableName(), columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            entity = mapCursorToEntity(cursor);
            cursor.close();
        }
        return entity;
    }

    protected abstract String getTableName();

    protected abstract T mapCursorToEntity(Cursor cursor);
}
