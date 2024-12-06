package com.example.btlon.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class RatingTableHelper extends BaseTableHelper<Rating> {
    private static final String TABLE_NAME = "ratings";
    private static final String COL_RATING_ID = "rating_id";
    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_RATING = "rating";
    private SqliteHelper sqliteHelper;

    public RatingTableHelper(Context context) {
        super(context);
        this.sqliteHelper = new SqliteHelper(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Rating mapCursorToEntity(Cursor cursor) {
        int ratingIdIndex = cursor.getColumnIndex(COL_RATING_ID);
        int productIdIndex = cursor.getColumnIndex(COL_PRODUCT_ID);
        int userIdIndex = cursor.getColumnIndex(COL_USER_ID);
        int ratingIndex = cursor.getColumnIndex(COL_RATING);

        if (ratingIdIndex >= 0 && productIdIndex >= 0 && userIdIndex >= 0 && ratingIndex >= 0) {
            int ratingId = cursor.getInt(ratingIdIndex);
            int productId = cursor.getInt(productIdIndex);
            int userId = cursor.getInt(userIdIndex);
            float rating = cursor.getFloat(ratingIndex);
            return new Rating(ratingId, productId, userId, rating);
        } else {
            Log.e("RatingTableHelper", "One or more columns are missing in the cursor");
            return null;
        }
    }



    // Lấy tất cả đánh giá của sản phẩm
    public ArrayList<Rating> getRatingsForProduct(int productId) {
        ArrayList<Rating> ratings = new ArrayList<>();
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        Cursor cursor = null;

        try {
            String selection = COL_PRODUCT_ID + "=?";
            String[] selectionArgs = {String.valueOf(productId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Rating rating = mapCursorToEntity(cursor);
                    if (rating != null) {
                        ratings.add(rating);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("RatingTableHelper", "Error fetching ratings: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d("RatingTableHelper", "Loaded " + ratings.size() + " ratings for productId " + productId);
        return ratings;
    }


    // Thêm mới hoặc cập nhật đánh giá của người dùng cho một sản phẩm cụ thể
    public boolean addOrUpdateRating(int userId, int productId, float newRating) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PRODUCT_ID, productId);
        values.put(COL_RATING, newRating);

        // Kiểm tra xem người dùng đã có đánh giá cho sản phẩm này chưa
        String selection = COL_USER_ID + "=? AND " + COL_PRODUCT_ID + "=?";
        String[] selectionArgs = {String.valueOf(userId), String.valueOf(productId)};
        Cursor cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

        long result;
        if (cursor != null && cursor.moveToFirst()) {
            // Nếu người dùng đã có đánh giá, thực hiện cập nhật
            result = db.update(TABLE_NAME, values, selection, selectionArgs);
        } else {
            // Nếu người dùng chưa có đánh giá, thực hiện thêm mới
            result = db.insert(TABLE_NAME, null, values);
        }

        cursor.close();

        return result != -1; // Trả về true nếu thao tác thành công, ngược lại false
    }

    // Lấy đánh giá của người dùng cho một sản phẩm cụ thể
    public float getRatingForUserAndProduct(int userId, int productId) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        Cursor cursor = null;
        float rating = 0;  // Giá trị mặc định nếu không tìm thấy đánh giá

        try {
            // Điều kiện truy vấn lấy đánh giá của người dùng cho sản phẩm cụ thể
            String selection = COL_USER_ID + "=? AND " + COL_PRODUCT_ID + "=?";
            String[] selectionArgs = {String.valueOf(userId), String.valueOf(productId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // Nếu có kết quả, lấy rating từ cursor
            if (cursor != null && cursor.moveToFirst()) {
                int ratingIndex = cursor.getColumnIndex(COL_RATING);
                if (ratingIndex >= 0) {
                    rating = cursor.getFloat(ratingIndex); // Lấy giá trị rating từ cursor
                }
            }
        } catch (Exception e) {
            Log.e("RatingTableHelper", "Error fetching rating for user and product: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return rating; // Trả về rating nếu tìm thấy, hoặc 0 nếu không có đánh giá
    }



    // Xóa đánh giá theo ratingId
    public boolean deleteRating(int ratingId) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_RATING_ID + "=?", new String[]{String.valueOf(ratingId)}) > 0;
    }
}
