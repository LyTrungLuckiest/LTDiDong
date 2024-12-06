package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.Data.Comment;
import com.example.btlon.R;

import java.util.List;

public class ProductCommentAdapter extends RecyclerView.Adapter<ProductCommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> comments;

    public ProductCommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        // Gán content vào txtComment
        holder.txtComment.setText(comment.getContent());

        // Chuyển userId thành chuỗi và gán vào txtUserId
        holder.txtUserId.setText(String.valueOf(comment.getUserId()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserId;

        TextView txtComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserId=itemView.findViewById(R.id.txtUser);
            txtComment = itemView.findViewById(R.id.txtComment);

        }
    }
}
