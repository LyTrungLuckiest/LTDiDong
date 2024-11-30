package com.example.btlon.Ui.Admin;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.btlon.R;

import com.example.btlon.Utils.AuthLogoutHelper;
import com.example.btlon.Utils.GoogleSignInHelper;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class AdminInfoFragment extends Fragment {
    private AuthLogoutHelper authLogoutHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_info_fragment, container, false);

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInHelper.getGoogleSignInOptions());
        authLogoutHelper = new AuthLogoutHelper(FirebaseAuth.getInstance(), googleSignInClient);

        Button btnLogout = rootView.findViewById(R.id.btnLogoutAdmin);

        btnLogout.setOnClickListener(v -> {

            authLogoutHelper.logout(getActivity());
        });
        

        return rootView;
    }


}
