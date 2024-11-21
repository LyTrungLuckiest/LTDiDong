package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "appsellFruit.db";
    private static final String TABLE_NAME = "Users";
    private static final String COL_2 = "username";
    private static final String COL_3 = "password";


    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH_SUFFIX = "/databases/";

    private Context context;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
//        deleteDatabaseFile(); // Xóa cơ sở dữ liệu
        copyDatabaseFromAssets();  // Sao chép cơ sở dữ liệu chỉ khi ứng dụng khởi động lần đầu
    }

    private String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void copyDatabaseFromAssets() {
        try {
            File dbFile = new File(getDatabasePath());
            if (!dbFile.exists()) {
                // Sao chép cơ sở dữ liệu từ assets vào thư mục databases
                InputStream myInput = context.getAssets().open(DATABASE_NAME);
                File dbFolder = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
                if (!dbFolder.exists()) {
                    dbFolder.mkdir();
                }

                OutputStream myOutput = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
                Log.d("Database", "Database copied successfully");
            }
        } catch (IOException e) {
            Log.e("Database", "Error copying database: " + e.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng "Users" nếu chưa có trong cơ sở dữ liệu
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (user_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nâng cấp cơ sở dữ liệu nếu có thay đổi
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi

        // Kiểm tra trùng lặp tên đăng nhập
        String[] columns = {COL_2};
        String selection = "username=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return false; // Trả về false nếu tên đăng nhập đã tồn tại
        }

        // Chèn tài khoản mới nếu không trùng
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1; // Trả về true nếu thêm thành công, false nếu thất bại
    }

    public String checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu ở chế độ đọc
        String[] columns = {COL_2};
        String selection = "username=? and password=?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        String result = null;

        if (cursor != null && cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndexOrThrow(COL_2));
            cursor.close();
        }
        return result;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int userId = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                String role = cursor.getString(3);

                User user = new User(userId, username, password, role);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());

        db.update("Users", values, "user_id = ?", new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    public void deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users", "user_id = ?", new String[]{String.valueOf(userId)});
        db.close();
    }


    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi
        db.delete(TABLE_NAME, null, null); // Xóa tất cả các hàng trong bảng "Users"
        db.close(); // Đóng cơ sở dữ liệu sau khi thao tác xóa
    }
    public void deleteDatabaseFile() {
        File dbFile = new File(getDatabasePath());
        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            if (deleted) {
                Log.d("Database", "Database file deleted successfully.");
            } else {
                Log.e("Database", "Failed to delete database file.");
            }
        } else {
            Log.e("Database", "Database file does not exist.");
        }
    }
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Products", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String price = cursor.getString(3);
                String image = cursor.getString(4); // Nếu lưu ID của hình ảnh trong drawable
                productList.add(new Product(image, name, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }


}
