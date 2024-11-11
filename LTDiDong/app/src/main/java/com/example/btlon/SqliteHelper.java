package com.example.btlon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Login.db";
    private static final String TABLE_NAME = "Users";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT UNIQUE, PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra trùng lặp tên đăng nhập
        String[] columns = {COL_2};
        String selection = "USERNAME=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false; // Trả về false nếu tên đăng nhập đã tồn tại
        }

        // Chèn tài khoản nếu không trùng
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);

        long result = db.insert("Users", null, contentValues);
        return result != -1; // Trả về true nếu thêm thành công, false nếu thất bại
    }


    public String checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_2};
        String selection = "USERNAME=? AND PASSWORD=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String result = null;

        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndexOrThrow(COL_2));
            cursor.close();
        }
        return result;
    }
    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi
        db.delete("Users", null, null); // Xóa tất cả các hàng trong bảng "Users"
        db.close(); // Đóng cơ sở dữ liệu sau khi thao tác xóa
    }
}
