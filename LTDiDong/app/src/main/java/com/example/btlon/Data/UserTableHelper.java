package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.btlon.Utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class UserTableHelper extends BaseTableHelper<Users> {
    private static final String TABLE_NAME = "Users";
    private static final String COL_ID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private SqliteHelper sqliteHelper;
    private Context Context;

    public UserTableHelper(Context context) {
        super(context);
        this.Context=context;
        this.sqliteHelper = new SqliteHelper(context);
        PreferenceManager preferenceManager = new PreferenceManager(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Users mapCursorToEntity(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
        String username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
        String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));
        return new Users(id, username, password, role);
    }

    public boolean checkLogin(String username, String password) {
        Log.d("UserTableHelper", "Đang kiểm tra đăng nhập cho người dùng: " + username);
        String[] columns = {COL_ID};
        String selection = COL_USERNAME + "=? AND " + COL_PASSWORD + "=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = sqliteHelper.getReadableDatabase().query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            Log.d("UserTableHelper", "Đăng nhập thất bại cho người dùng: " + username);
            return false;
        }
    }

    // Phương thức mới: Lấy userId dựa trên username
    public int getUserIdByUsername(String username) {
        int userId = -1; // Giá trị mặc định nếu không tìm thấy

        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn để lấy user_id
            String query = "SELECT " + COL_ID + " FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + " = ?";
            cursor = db.rawQuery(query, new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi lấy userId từ username: " + username, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userId;
    }

    public boolean addUser(String username, String password) {
        if (isUsernameExists(username)) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        boolean result = insert(values);
        return true;
    }

    private boolean isUsernameExists(String username) {
        String[] columns = {COL_ID};
        String selection = COL_USERNAME + "=?";
        String[] selectionArgs = {username};
        return getOne(columns, selection, selectionArgs) != null;
    }

    public List<Users> getAllUsers() {
        return getAll(new String[]{COL_ID, COL_USERNAME, COL_PASSWORD, COL_ROLE}, null, null, null);
    }

    public boolean updateUser(int userId, String username, String password, String role) {
        if(userId == Integer.parseInt(PreferenceManager.getUserId())) {
            Toast.makeText(Context, "Không thể cập nhật tài khoản đang đăng nhập", Toast.LENGTH_SHORT).show();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);
        return update(values, COL_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public boolean deleteUser(int userId) {
        if(userId == Integer.parseInt(PreferenceManager.getUserId())) {
            Toast.makeText(Context, "Không thể xóa tài khoản đang đăng nhập", Toast.LENGTH_SHORT).show();
               return false;
        }
        return delete(COL_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public Users getOne(String[] columns, String selection, String[] selectionArgs) {
        Cursor cursor = sqliteHelper.getReadableDatabase().query(
                getTableName(),
                columns,
                selection,
                selectionArgs,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            return mapCursorToEntity(cursor);
        }
        return null;
    }

    public boolean insert(ContentValues values) {
        long result = sqliteHelper.getWritableDatabase().insert(getTableName(), null, values);
        return result > 0;
    }

    public boolean update(ContentValues values, String whereClause, String[] whereArgs) {
        return sqliteHelper.getWritableDatabase().update(getTableName(), values, whereClause, whereArgs) > 0;
    }

    public boolean delete(String whereClause, String[] whereArgs) {
        return sqliteHelper.getWritableDatabase().delete(getTableName(), whereClause, whereArgs) > 0;
    }
    // Thêm phương thức lấy role của người dùng từ userId
    public String checkRole(String userId) {
        String role = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn lấy role từ bảng Users theo userId
            String query = "SELECT " + COL_ROLE + " FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{userId});

            if (cursor != null && cursor.moveToFirst()) {
                role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi lấy role từ userId: " + userId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return role;
    }

    public boolean checkUsernameExists(String newUsername) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;
        boolean exists = false;

        try {
            // Truy vấn để kiểm tra username
            String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + " = ?";
            cursor = db.rawQuery(query, new String[]{newUsername});

            // Nếu cursor trả về kết quả, username đã tồn tại
            exists = (cursor != null && cursor.moveToFirst());
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi kiểm tra username tồn tại: " + newUsername, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return exists;
    }
    public Users getUserById(int userId) {
        Users user = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn lấy thông tin người dùng dựa trên userId
            String query = "SELECT * FROM Users WHERE " + COL_ID + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy thông tin người dùng từ cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                // Bạn có thể thêm các thông tin khác nếu bảng Users có nhiều cột hơn
                user = new Users(id);
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi lấy thông tin người dùng từ userId: " + userId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return user;
    }



}
