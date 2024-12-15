package com.example.btlon.Ui.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.btlon.Data.UserTableHelper;
import com.example.btlon.Data.Users;
import com.example.btlon.R;
import com.example.btlon.Ui.Admin.AdminActivity;
import com.example.btlon.Utils.PreferenceManager;
import com.example.btlon.Utils.SearchResultsActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private ImageButton dropdownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);


        UserTableHelper userTableHelper = new UserTableHelper(this);
        PreferenceManager preferenceManager = new PreferenceManager(this);

        // Kiểm tra xem người dùng đã đăng nhập và là admin chưa
        if (preferenceManager.isLoggedIn() && (Objects.equals(userTableHelper.checkRole(preferenceManager.getUserId()), "Admin")||Objects.equals(userTableHelper.checkRole(preferenceManager.getUserId()), "Staff"))) {
            Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
            startActivity(intent);
            return;  // Dừng lại không tiếp tục các logic sau
        }


        // Khởi tạo BottomNavigationView và NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerViewHome);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        // Đảm bảo BottomNavigationView không tự động điều hướng nếu không cần thiết
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.productFragment:
                    // Điều hướng đến ProductFragment, không tự động đến CartFragment
                    navController.navigate(R.id.productFragment);
                    return true;
                case R.id.cartFragment:
                    // Điều hướng đến CartFragment nếu cần
                    navController.navigate(R.id.cartFragment);
                    return true;
                case R.id.saleFragment:
                    // Điều hướng đến , không tự động đến CartFragment
                    navController.navigate(R.id.saleFragment);
                    return true;
                case R.id.userFragment:
                    // Điều hướng đến , không tự động đến CartFragment
                    navController.navigate(R.id.userFragment);
                    return true;
                default:
                    return false;
            }
        });
        ImageButton btnGiohang = findViewById(R.id.btnGiohang);

        btnGiohang.setOnClickListener(v -> {
            navController.navigate(R.id.cartFragment);
            bottomNavigationView.setSelectedItemId(R.id.cartFragment);
        });
        // Xử lý deeplink nếu có
        handleDeepLink(getIntent());

        // Điều hướng giỏ hàng nếu cần
        handleCartNavigation();

        // Ẩn BottomNavigationView với một số Fragment
        HideBottomNavigation(bottomNavigationView, navController);

        // Khởi tạo SearchView
        SearchView searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchResultsActivity.class);
                intent.putExtra("search_query", query);
                startActivity(intent);
                searchView.setQuery("", false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());

        // Nhận diện giọng nói
        ImageButton btnMicrophone = findViewById(R.id.btnGiongnoi);
        btnMicrophone.setOnClickListener(v -> startVoiceRecognition());

        // Menu dropdow



    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật Intent mới với thông tin mới

        // Xử lý deeplink
        handleDeepLink(intent);

        // Kiểm tra nếu có điều hướng giỏ hàng (CartFragment)
        handleCartNavigation();
    }

    // Xử lý deeplink
    private void handleDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            String transactionStatus = data.getQueryParameter("status");
            if ("success".equals(transactionStatus)) {
                Toast.makeText(this, "Giao dịch thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Giao dịch thất bại.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Điều hướng giỏ hàng nếu có
    private void handleCartNavigation() {
        boolean isCartTransition = getIntent().getBooleanExtra("isCartTransition", false);
        if (isCartTransition) {
            NavController navController = Navigation.findNavController(this, R.id.fragmentContainerViewHome);
            navController.navigate(R.id.cartFragment);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
            bottomNavigationView.setSelectedItemId(R.id.cartFragment);

            // Xóa flag sau khi điều hướng
            getIntent().removeExtra("isCartTransition");
        }
    }

    // Ẩn BottomNavigationView với một số Fragment
    private static void HideBottomNavigation(BottomNavigationView bottomNavigationView, NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int[] fragmentsToHideBottomNav = {
                    R.id.userInfoFragment,
                    R.id.userAddressFragment,
                    R.id.userOderFragment,
                    R.id.userGiftFragment,
                    R.id.userFeedBackFragment,
                    R.id.productDetail,
            };

            boolean shouldHide = false;
            for (int id : fragmentsToHideBottomNav) {
                if (destination.getId() == id) {
                    shouldHide = true;
                    break;
                }
            }
            bottomNavigationView.setVisibility(shouldHide ? View.GONE : View.VISIBLE);
        });
    }



    private void startVoiceRecognition() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Nhận diện giọng nói không khả dụng trên thiết bị này.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi-VN"); // Đặt ngôn ngữ là Tiếng Việt
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khởi tạo nhận diện giọng nói: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void performVoiceSearch(String query) {
        // Chuyển đến màn hình tìm kiếm với kết quả từ nhận diện giọng nói
        Intent intent = new Intent(HomeActivity.this, SearchResultsActivity.class);
        intent.putExtra("search_query", query);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                Toast.makeText(this, "Đã nhận diện: " + recognizedText, Toast.LENGTH_SHORT).show();

                // Gọi chức năng tìm kiếm sản phẩm
                performVoiceSearch(recognizedText);
            } else {
                Toast.makeText(this, "Không nhận diện được giọng nói.", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Nhận diện giọng nói đã bị hủy.", Toast.LENGTH_SHORT).show();
        }
    }


}
