package com.example.btlon.Ui.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import android.widget.SearchView;
import android.widget.Toast;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;

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

        if (preferenceManager.isLoggedIn() && Objects.equals(userTableHelper.checkRole(preferenceManager.getUserId()), "Admin")) {
            // Chuyển hướng đến AdminActivity nếu người dùng đã đăng nhập và là admin
            Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
            startActivity(intent);
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerViewHome);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);




        // Khởi tạo SearchView
        SearchView searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Chuyển sang SearchResultsActivity với từ khóa tìm kiếm
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

        Log.d("HomeActivity", "Hoạt động Home đã được bắt đầu!");

        ImageButton btnMicrophone = findViewById(R.id.btnGiongnoi);
        dropdownButton = findViewById(R.id.menu);

        // Nhận diện giọng nói
        btnMicrophone.setOnClickListener(v -> startVoiceRecognition());

        // Bấm vào giỏ hàng (sản phẩm) và chuyển hướng đến CartFragment
        ImageButton btnGiohang = findViewById(R.id.btnGiohang);
        btnGiohang.setOnClickListener(v -> {
            // Điều hướng trực tiếp đến CartFragment
            navController.navigate(R.id.cartFragment); // Giả sử cartFragment là ID của CartFragment trong nav_graph.xml
            bottomNavigationView.setSelectedItemId(R.id.cartFragment); // Đảm bảo giỏ hàng được chọn trong BottomNavigationView
        });

        // Điều hướng BottomNavigationView
        HideBottomNavigation(bottomNavigationView, navController);
        DropDownbuttonClick();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Cập nhật Intent mới với thông tin mới.
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerViewHome);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        // Kiểm tra lại flag
        boolean isCartTransition = getIntent().getBooleanExtra("isCartTransition", false);
        Log.d("HomeActivity", "onNewIntent - isCartTransition: " + isCartTransition);

        if (isCartTransition) {
            // Ensure navigation happens here as well
            navController.navigate(R.id.cartFragment);
            bottomNavigationView.setSelectedItemId(R.id.cartFragment);
            // Remove flag after transition
            getIntent().removeExtra("isCartTransition");
        }
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
    }
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

    private void DropDownbuttonClick() {
        dropdownButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, dropdownButton);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.option_1:
                        Toast.makeText(this, "Lựa chọn 1 đã được chọn", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_2:
                        Toast.makeText(this, "Lựa chọn 2 đã được chọn", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_3:
                        Toast.makeText(this, "Lựa chọn 3 đã được chọn", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_4:
                        Toast.makeText(this, "Lựa chọn 4 đã được chọn", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            });

            popupMenu.setOnDismissListener(menu -> dropdownButton.setImageResource(R.drawable.menu));
            dropdownButton.setImageResource(R.drawable.baseline_cancel_24);
            popupMenu.show();
        });
    }


    private void startVoiceRecognition() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Nhận diện giọng nói không khả dụng trên thiết bị này.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó...");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                Toast.makeText(this, "Đã nhận diện: " + recognizedText, Toast.LENGTH_SHORT).show();
            }
        }
    }


}
