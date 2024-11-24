package com.example.btlon.Ui.Home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;


import com.example.btlon.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    private ImageButton dropdownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());

        Log.d("HomeActivity", "Hoạt động Home đã được bắt đầu!");

        ImageButton btnMicrophone = findViewById(R.id.btnGiongnoi);
        dropdownButton = findViewById(R.id.button_dropdown);

        btnMicrophone.setOnClickListener(v -> startVoiceRecognition());

        ImageButton btnGiohang = findViewById(R.id.btnGiohang);
        btnGiohang.setOnClickListener(v -> {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
            bottomNavigationView.setSelectedItemId(R.id.cartFragment);
        });

        //Trung lam
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerViewHome);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        HideBottomNavigation(bottomNavigationView, navController);

        DropDownbuttonClick();
    }

    private static void HideBottomNavigation(BottomNavigationView bottomNavigationView, NavController navController) {
        // Lắng nghe thay đổi destination để ẩn/hiện BottomNavigationView
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Danh sách các Fragment cần ẩn BottomNavigationView
            int[] fragmentsToHideBottomNav = {
                    R.id.userInfoFragment,
                    R.id.userAddressFragment,
                    R.id.userOderFragment,
                    R.id.userGiftFragment,
                    R.id.userFeedBackFragment,
                    R.id.userPointFragment
            };

            // Ẩn BottomNavigationView nếu destination nằm trong danh sách
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
