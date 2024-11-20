package com.example.btlon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class UserFragment extends Fragment {

    private AuthLogoutHelper authLogoutHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        // Lấy GoogleSignInClient từ Activity chứa Fragment
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInHelper.getGoogleSignInOptions());

        // Khởi tạo AuthLogoutHelper với FirebaseAuth và GoogleSignInClient
        authLogoutHelper = new AuthLogoutHelper(FirebaseAuth.getInstance(), googleSignInClient);

        // Lấy Button từ layout của fragment
        Button btnLogout = rootView.findViewById(R.id.btnLogout);

        // Gắn sự kiện cho nút Đăng xuất
        btnLogout.setOnClickListener(v -> authLogoutHelper.logout(getActivity()));

        return rootView;
    }
}
