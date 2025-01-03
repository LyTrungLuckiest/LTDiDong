package com.example.btlon.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
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
    private static final String COL_NAME = "name";
    private static final String COL_PHONE = "phone_number";

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
        String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE));
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
        return insert(values); // Trả về kết quả thực tế từ phương thức insert
    }


    public boolean updateUserName(int userId, String name, String phone) {
        // Khởi tạo ContentValues để lưu trữ giá trị
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_PHONE, phone);

        // Thêm log để kiểm tra thông tin được truyền vào
        Log.d("UserTableHelper", "Cập nhật thông tin người dùng: userId = " + userId + ", name = " + name + ", phone = " + phone);

        // Cập nhật tên người dùng theo userId
        boolean isUpdated = update(values, COL_ID + "=?", new String[]{String.valueOf(userId)});

        // Thêm log để kiểm tra kết quả của câu lệnh update
        if (isUpdated) {
            Log.d("UserTableHelper", "Cập nhật thành công cho userId = " + userId);
        } else {
            Log.d("UserTableHelper", "Cập nhật không thành công cho userId = " + userId);
        }

        return isUpdated;
    }



    private boolean isUsernameExists(String username) {
        String[] columns = {COL_ID};
        String selection = COL_USERNAME + "=?";
        String[] selectionArgs = {username};

        Cursor cursor = sqliteHelper.getReadableDatabase().query(
                getTableName(), columns, selection, selectionArgs, null, null, null);

        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close(); // Đảm bảo đóng cursor
        }
        return exists;
    }

    public List<Users> getAllUsers() {
        List<Users> usersList = new ArrayList<>();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            // Lấy chỉ số cột cho từng trường, đảm bảo rằng cột tồn tại
            int userIdIndex = cursor.getColumnIndex(COL_ID);  // Lấy chỉ số cột user_id
            int usernameIndex = cursor.getColumnIndex(COL_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);
            int roleIndex = cursor.getColumnIndex(COL_ROLE);

            // Kiểm tra xem các chỉ số cột có hợp lệ không (>= 0)
            if (userIdIndex >= 0 && usernameIndex >= 0 && passwordIndex >= 0 && roleIndex >= 0) {
                do {
                    int userId = cursor.getInt(userIdIndex);  // Lấy giá trị user_id
                    String username = cursor.getString(usernameIndex);
                    String password = cursor.getString(passwordIndex);
                    String role = cursor.getString(roleIndex);

                    // Kiểm tra xem username có hợp lệ không
                    if (!TextUtils.isEmpty(username)) {
                        Users user = new Users(userId, username, password, role); // Cập nhật để tạo đối tượng Users với userId
                        usersList.add(user);
                    }
                } while (cursor.moveToNext());
            } else {
                Log.e("SQLite", "Một hoặc nhiều cột không tồn tại trong cơ sở dữ liệu!");
            }
        }
        cursor.close();
        db.close();
        return usersList;
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
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?";
            Log.d("SQL Debug", "Truy vấn: " + query + ", userId: " + userId);
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy thông tin người dùng từ cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));

                user = new Users(id, username, password, role);
            } else {
                Log.d("UserTableHelper", "Không tìm thấy user với ID: " + userId);
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi lấy thông tin người dùng từ userId: " + userId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
    public Users getUserNamePhoneById(int userId) {
        Users user = null;
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Truy vấn lấy thông tin người dùng dựa trên userId
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?";
            Log.d("SQL Debug", "Truy vấn: " + query + ", userId: " + userId);
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Lấy thông tin người dùng từ cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLE));
                String name=cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String phone=cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE));



                user = new Users(id, username, password, role,name,phone);
            } else {
                Log.d("UserTableHelper", "Không tìm thấy user với ID: " + userId);
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi lấy thông tin người dùng từ userId: " + userId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
    public boolean addUpdateUserAddress(int userId, String address) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        boolean isUpdated = false;

        try {
            db = sqliteHelper.getWritableDatabase(); // Get the writable database

            // Check if an address already exists for the given user
            String query = "SELECT * FROM Users WHERE user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Address exists, update it
                ContentValues values = new ContentValues();
                values.put("address", address);
                int rowsUpdated = db.update("Users", values, "user_id = ?", new String[]{String.valueOf(userId)});
                if (rowsUpdated > 0) {
                    isUpdated = true;
                    Log.d("UserTableHelper", "Đã cập nhật địa chỉ cho người dùng với ID: " + userId);
                }
            } else {
                // Address doesn't exist, insert a new one
                ContentValues values = new ContentValues();
                values.put("user_id", userId);
                values.put("address", address);
                long result = db.insert("address", null, values);
                if (result > 0) {
                    isUpdated = true;
                    Log.d("UserTableHelper", "Đã thêm địa chỉ cho người dùng với ID: " + userId);
                } else {
                    Log.e("UserTableHelper", "Không thể thêm địa chỉ cho người dùng với ID: " + userId);
                }
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi thêm hoặc cập nhật địa chỉ cho người dùng với ID: " + userId, e);
        } finally {
            if (cursor != null) {
                cursor.close(); // Ensure the cursor is closed
            }
            if (db != null) {
                db.close(); // Ensure the database is closed
            }
        }

        return isUpdated;
    }


    public String getUserAddressById(int userId) {
        String address = ""; // Default to empty string if no address is found
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to fetch address based on userId from the Users table
            String query = "SELECT address FROM Users WHERE user_id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // If an address is found, assign it to the 'address' variable
                address = cursor.getString(cursor.getColumnIndexOrThrow("address"));

                // Log the address
                Log.d("UserTableHelper", "Địa chỉ của người dùng với ID " + userId + " là: " + address);
            } else {
                Log.d("UserTableHelper", "Không tìm thấy địa chỉ cho người dùng với ID: " + userId);
            }
        } catch (Exception e) {
            Log.e("UserTableHelper", "Lỗi khi lấy địa chỉ cho người dùng với ID: " + userId, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return address; // Return either the address or an empty string
    }





}
