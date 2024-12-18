package com.example.btlon.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommentTableHelper extends BaseTableHelper<Comment> {
    private static final String TABLE_NAME = "Comment";
    private static final String COL_COMMENT_ID = "comment_id";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_CONTENT = "content";
    private static final String COL_CREATED_AT = "created_at";

    public CommentTableHelper(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected Comment mapCursorToEntity(Cursor cursor) {
        int commentIdIndex = cursor.getColumnIndex(COL_COMMENT_ID);
        int userIdIndex = cursor.getColumnIndex(COL_USER_ID);
        int productIdIndex = cursor.getColumnIndex(COL_PRODUCT_ID);
        int contentIndex = cursor.getColumnIndex(COL_CONTENT);
        int createdAtIndex = cursor.getColumnIndex(COL_CREATED_AT);

        // Check if columns exist
        if (commentIdIndex == -1 || userIdIndex == -1 || productIdIndex == -1 || contentIndex == -1 || createdAtIndex == -1) {
            Log.e("CommentTableHelper", "One or more columns are missing in the cursor");
            return null; // Return null or handle error
        }

        int commentId = cursor.getInt(commentIdIndex);
        int userId = cursor.getInt(userIdIndex);
        int productId = cursor.getInt(productIdIndex);
        String content = cursor.getString(contentIndex);
        String createdAt = cursor.getString(createdAtIndex);

        return new Comment(commentId, userId, productId, content, createdAt);
    }


    // Thêm bình luận mới
    public boolean insertComment(int userId, int productId, String content) {
        ContentValues values = new ContentValues();
        values.put(COL_USER_ID, userId);
        values.put(COL_PRODUCT_ID, productId);
        values.put(COL_CONTENT, content);
        return insert(values);
    }

    // Lấy tất cả bình luận theo productId
    public List<Comment> getCommentsByProductId(int productId) {
        List<Comment> comments = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = COL_PRODUCT_ID + "=?";
            String[] selectionArgs = {String.valueOf(productId)};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, COL_CREATED_AT + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Comment comment = mapCursorToEntity(cursor);
                    if (comment != null) {
                        comments.add(comment);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("CommentTableHelper", "Error fetching comments: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return comments;
    }

    // Xóa bình luận theo commentId
    public boolean deleteComment(int commentId) {
        return delete(COL_COMMENT_ID + "=?", new String[]{String.valueOf(commentId)});
    }
}
