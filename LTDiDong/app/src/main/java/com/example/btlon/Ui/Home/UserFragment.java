package com.example.btlon.Ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.btlon.R;
import com.example.btlon.Utils.AuthLogoutHelper;
import com.example.btlon.Utils.GoogleSignInHelper;
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

        Button btnLogout = rootView.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> authLogoutHelper.logout(getActivity()));

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
