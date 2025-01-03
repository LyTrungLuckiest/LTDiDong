package com.example.btlon.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementTableHelper {

    private SQLiteDatabase database;
    private String COL_ID = "id";
    private final String COL_URL = "url";
    private final String TABLE_NAME = "Advertisement";

    public AdvertisementTableHelper(Context context) {
        SqliteHelper dbHelper = new SqliteHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public long addAdvertisement(String url) {
        if (url == null || url.isEmpty()) {
            Log.e("Advertisement", "URL không thể là null hoặc rỗng");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(COL_URL, url);

        try {
            return database.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi thêm quảng cáo: " + e.getMessage());
            return -1;
        }
    }


    public void deleteAdvertisement(int id) {
        try {
            database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi xóa quảng cáo: " + e.getMessage());
        }
    }


    public int updateAdvertisement(int id, String newUrl) {
        if (newUrl == null || newUrl.isEmpty()) {
            Log.e("Advertisement", "URL không thể là null hoặc rỗng");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(COL_URL, newUrl);

        try {
            return database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi cập nhật quảng cáo: " + e.getMessage());
            return -1;
        }
    }


    public List<String> getAdvertisements() {
        List<String> advertisementList = new ArrayList<>();
        Cursor cursor = null;

        try {
            String query = "SELECT url FROM Advertisement";
            cursor = database.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String url = cursor.getString(cursor.getColumnIndexOrThrow(COL_URL));
                    advertisementList.add(url);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi lấy danh sách quảng cáo: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return advertisementList;
    }


    public boolean checkAdvertisementExists(String url) {
        boolean exists = false;
        Cursor cursor = null;

        try {
            cursor = database.query(TABLE_NAME, new String[]{COL_URL}, "url = ?", new String[]{url}, null, null, null);
            exists = cursor.moveToFirst();
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi kiểm tra sự tồn tại của quảng cáo: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return exists;
    }


    public void deleteAllAdvertisements() {
        try {
            database.delete(TABLE_NAME, null, null);
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi xóa tất cả quảng cáo: " + e.getMessage());
        }
    }


    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public String getCOL_ID() {
        return COL_ID;
    }

    public void setCOL_ID(String COL_ID) {
        this.COL_ID = COL_ID;
    }
}
