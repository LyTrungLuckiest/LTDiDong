package com.example.btlon.Ui.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.btlon.R;
import com.example.btlon.Ui.Login.LoginActivity;
import com.example.btlon.Utils.AuthLogoutHelper;
import com.example.btlon.Utils.GoogleSignInHelper;
import com.example.btlon.Utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class UserFragment extends Fragment {

    private AuthLogoutHelper authLogoutHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_user_fragment, container, false);

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInHelper.getGoogleSignInOptions());
        authLogoutHelper = new AuthLogoutHelper(FirebaseAuth.getInstance(), googleSignInClient);
        PreferenceManager preferenceManager = new PreferenceManager(getActivity());

        // Lấy các thành phần trong giao diện
        Button btnLogout = rootView.findViewById(R.id.btnLogout);
        Button btnDangnhap = rootView.findViewById(R.id.btnDangnhap);
        LinearLayout userInfoLayout = rootView.findViewById(R.id.infouser); // Lấy LinearLayout chứa thông tin người dùng
        LinearLayout userPointLayout = rootView.findViewById(R.id.userPointLayout);

        // Kiểm tra trạng thái đăng nhập và ẩn/hiện các phần tử
        if (preferenceManager.isLoggedIn()) {
            btnDangnhap.setVisibility(View.GONE);  // Ẩn nút Đăng nhập nếu đã đăng nhập
            userInfoLayout.setVisibility(View.VISIBLE);
            userPointLayout.setVisibility(View.VISIBLE);// Hiển thị thông tin người dùng nếu đã đăng nhập
            btnLogout.setVisibility(View.VISIBLE);  // Hiển thị nút Đăng xuất nếu đã đăng nhập
        } else {
            btnDangnhap.setVisibility(View.VISIBLE);    // Hiển thị nút Đăng nhập nếu chưa đăng nhập
            userInfoLayout.setVisibility(View.GONE);
            userPointLayout.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);    // Ẩn nút Đăng xuất nếu chưa đăng nhập
        }
        btnDangnhap.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        // Xử lý sự kiện đăng xuất
        btnLogout.setOnClickListener(v -> {
            if (preferenceManager.isLoggedIn()) {
                authLogoutHelper.logout(getActivity()); // Thực hiện đăng xuất
            } else {
                Toast.makeText(getActivity(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });

        // Tìm Button trong giao diện Fragment
        Button btnCall = rootView.findViewById(R.id.btnCall);

        // Gắn sự kiện bấm cho Button
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "0363546978"; // Số điện thoại
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTransition(view);
    }



    private static void FragmentTransition(@NonNull View view) {
        NavController navController = Navigation.findNavController(view);
        view.findViewById(R.id.btUserInfo).setOnClickListener(v -> {
            navController.navigate(R.id.action_userFragment_to_userInfoFragment);
        });
        view.findViewById(R.id.btUserAddress).setOnClickListener(v -> {
            navController.navigate(R.id.action_userFragment_to_userAddressFragment);
        });
        view.findViewById(R.id.btUserOrder).setOnClickListener(v -> {
            navController.navigate(R.id.action_userFragment_to_userOderFragment);
        });
        view.findViewById(R.id.btUserGift).setOnClickListener(v -> {
            navController.navigate(R.id.action_userFragment_to_userGiftFragment);
        });
        view.findViewById(R.id.btUserFeedBack).setOnClickListener(v -> {
            navController.navigate(R.id.action_userFragment_to_userFeedBackFragment);
        });
        view.findViewById(R.id.btUserPoint).setOnClickListener(v -> {
            navController.navigate(R.id.action_userFragment_to_userPointFragment);
        });
    }
}
