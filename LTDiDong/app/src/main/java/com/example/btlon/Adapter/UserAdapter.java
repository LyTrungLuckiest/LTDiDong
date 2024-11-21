package com.example.btlon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlon.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private OnUserActionListener onUserActionListener;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void setOnUserActionListener(OnUserActionListener listener) {
        this.onUserActionListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtId.setText(String.valueOf(user.getUserId()));
        holder.txtName.setText(user.getUsername());
        holder.txtPassword.setText(user.getPassword());

        // Gắn hành động cho nút btAction
        holder.btAction.setOnClickListener(v -> {
            if (onUserActionListener != null) {
                onUserActionListener.onAction(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtName, txtPassword;
        Button btAction;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtId);
            txtName = itemView.findViewById(R.id.txtName);
            txtPassword = itemView.findViewById(R.id.txtPassword);
            btAction = itemView.findViewById(R.id.btAction);
        }
    }

    // Listener interface cho hành động trên user
    public interface OnUserActionListener {
        void onAction(User user, int position);
    }
}
