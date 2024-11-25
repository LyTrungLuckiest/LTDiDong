package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserTableHelper extends BaseTableHelper<Users> {
    private static final String TABLE_NAME = "Users";
    private static final String COL_ID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE = "role";
    private SqliteHelper sqliteHelper;

    public UserTableHelper(Context context) {
        super(context);
        this.sqliteHelper = new SqliteHelper(context);
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
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        values.put(COL_ROLE, role);
        return update(values, COL_ID + "=?", new String[]{String.valueOf(userId)});
    }


    // Cập nhật địa chỉ người dùng
    public void updateUserAddress(int userId, String address) {
        sqliteHelper.getWritableDatabase(); // Khai báo db
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address); // Cập nhật địa chỉ

        // Cập nhật địa chỉ cho người dùng có user_id = userId
        int rowsAffected = db.update("Users", contentValues, "user_id = ?", new String[]{String.valueOf(userId)});

        if (rowsAffected > 0) {
            Log.d("UserTableHelper", "Cập nhật địa chỉ thành công cho user_id: " + userId);
        } else {
            Log.d("UserTableHelper", "Không tìm thấy user_id: " + userId + " để cập nhật địa chỉ");
        }
        db.close(); // Đóng kết nối db sau khi thực hiện xong
    }

    public boolean deleteUser(int userId) {
        return delete(COL_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public List<Users> getUserByCriteria(String criteria, String[] args) {
        return getAll(new String[]{COL_ID, COL_USERNAME, COL_PASSWORD, COL_ROLE}, criteria, args, null);
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

    public List<Users> getAll(String[] columns, String selection, String[] selectionArgs, String limit) {
        Cursor cursor = sqliteHelper.getReadableDatabase().query(
                getTableName(),
                columns,
                selection,
                selectionArgs,
                null, null, limit
        );

        return mapCursorToList(cursor);
    }

    private List<Users> mapCursorToList(Cursor cursor) {
        List<Users> users = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                users.add(mapCursorToEntity(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return users;
    }
}
