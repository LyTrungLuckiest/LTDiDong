package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appsellFruit.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";
    private Context context;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
     // deleteDatabase();
        copyDatabaseFromAssets();
    }

    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDatabaseFromAssets() {
        try {
            File dbFile = new File(getDatabasePath());
            if (!dbFile.exists()) {
                InputStream inputStream = context.getAssets().open(DATABASE_NAME);
                File dbFolder = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbFolder.exists()) {
                    dbFolder.mkdir();
                }
                OutputStream outputStream = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                Log.d("Cơ sở dữ liệu", "Sao chép cơ sở dữ liệu thành công");
            }
        } catch (IOException e) {
            Log.e("Cơ sở dữ liệu", "Lỗi sao chép cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public boolean deleteDatabase() {
        File dbFile = new File(getDatabasePath());
        if (dbFile.exists()) {
            boolean isDeleted = dbFile.delete();
            if (isDeleted) {
                Log.d("Cơ sở dữ liệu", "Xóa cơ sở dữ liệu thành công");
            } else {
                Log.e("Cơ sở dữ liệu", "Xóa cơ sở dữ liệu thất bại");
            }
            return isDeleted;
        } else {
            Log.e("Cơ sở dữ liệu", "Tệp cơ sở dữ liệu không tồn tại");
            return false;
        }
    }
    public void updateUserAddress(int userId, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address); // Cập nhật địa chỉ

        // Cập nhật địa chỉ cho người dùng có user_id = userId
        db.update("Users", contentValues, "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
