package com.example.btlon.Data;

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
        database = dbHelper.getWritableDatabase(); // Sử dụng writable database để thêm, sửa, xóa dữ liệu
    }

    // Thêm quảng cáo mới vào bảng
    public long addAdvertisement(String url) {
        if (url == null || url.isEmpty()) {
            Log.e("Advertisement", "URL không thể là null hoặc rỗng");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(COL_URL, url); // Chỉ cần trường 'url'

        try {
            return database.insert(TABLE_NAME, null, values); // Trả về ID của bản ghi vừa thêm
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi thêm quảng cáo: " + e.getMessage());
            return -1;
        }
    }

    // Xóa quảng cáo theo ID
    public void deleteAdvertisement(int id) {
        try {
            database.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi xóa quảng cáo: " + e.getMessage());
        }
    }

    // Cập nhật quảng cáo theo ID
    public int updateAdvertisement(int id, String newUrl) {
        if (newUrl == null || newUrl.isEmpty()) {
            Log.e("Advertisement", "URL không thể là null hoặc rỗng");
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(COL_URL, newUrl); // Cập nhật URL mới

        try {
            return database.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi cập nhật quảng cáo: " + e.getMessage());
            return -1;
        }
    }

    // Lấy danh sách tất cả quảng cáo
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
                cursor.close(); // Đảm bảo đóng cursor sau khi sử dụng
            }
        }

        return advertisementList;
    }

    // Kiểm tra sự tồn tại của quảng cáo trong bảng (dựa trên URL)
    public boolean checkAdvertisementExists(String url) {
        boolean exists = false;
        Cursor cursor = null;

        try {
            cursor = database.query(TABLE_NAME, new String[]{COL_URL}, "url = ?", new String[]{url}, null, null, null);
            exists = cursor.moveToFirst(); // Nếu tìm thấy hàng dữ liệu, quảng cáo tồn tại
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi kiểm tra sự tồn tại của quảng cáo: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close(); // Đảm bảo đóng cursor sau khi sử dụng
            }
        }

        return exists;
    }

    // Xóa tất cả các quảng cáo
    public void deleteAllAdvertisements() {
        try {
            database.delete(TABLE_NAME, null, null); // Xóa toàn bộ bảng
        } catch (SQLException e) {
            Log.e("Advertisement", "Lỗi khi xóa tất cả quảng cáo: " + e.getMessage());
        }
    }

    // Đóng kết nối cơ sở dữ liệu (nên làm khi không còn sử dụng)
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
