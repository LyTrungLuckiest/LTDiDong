package com.example.btlon.Ui.Home;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;


import com.example.btlon.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
//micro
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Locale;



public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    ImageButton dropdownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        // Khởi tạo Facebook SDK
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());


        Log.d("HomeActivity", "Home Activity is started!");



        ImageButton btnMicrophone = findViewById(R.id.btnGiongnoi);
        dropdownButton = findViewById(R.id.button_dropdown);


        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognition();
            }
        });


        ImageButton btnGiohang = findViewById(R.id.btnGiohang);
        btnGiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến tab CartFragment
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationHome);
                bottomNavigationView.setSelectedItemId(R.id.cartFragment);
            }
        });

        // Thiết lập NavController cho BottomNavigationView
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationHome);
        NavController navController = (NavController) Navigation.findNavController(this, R.id.fragmentContainerViewHome);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        dropdownButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this, dropdownButton);

            // Xác định chính xác tệp menu từ XML
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());

            // Xử lý sự kiện click vào item
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.option_1:
                        Toast.makeText(this, "Option 1 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_2:
                        Toast.makeText(this, "Option 2 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_3:
                        Toast.makeText(this, "Option 3 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.option_4:
                        Toast.makeText(this, "Option 4 selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            });

            // Lắng nghe sự kiện hiển thị menu
            popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                @Override
                public void onDismiss(PopupMenu menu) {
                    // Khôi phục lại icon ban đầu khi menu bị ẩn
                    dropdownButton.setImageResource(R.drawable.menu); // Icon gốc
                }
            });

            // Đổi icon khi menu hiển thị
            dropdownButton.setImageResource(R.drawable.baseline_cancel_24); // Icon khi menu mở
            popupMenu.show(); // Hiển thị popup menu
        });




    }

    private void startVoiceRecognition() {
        // Check if the device supports speech recognition
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech Recognition is not available on this device.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create an Intent to start the speech recognition
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak something...");

        // Start the activity to listen for speech input
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                // You can now use the recognizedText, for example, display it in a Toast
                Toast.makeText(this, "Recognized: " + recognizedText, Toast.LENGTH_SHORT).show();
                // Or perform a search in your app
            }
        }
    }


}