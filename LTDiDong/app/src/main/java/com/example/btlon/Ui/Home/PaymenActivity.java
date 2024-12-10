package com.example.btlon.Ui.Home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btlon.R;

public class PaymenActivity extends AppCompatActivity {

    TextView textNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymen);
        textNotification= findViewById(R.id.textViewNotify);
        Intent intent= getIntent();
      textNotification.setText(  intent.getStringExtra("result"));
        String result = intent.getStringExtra("result");
        if (result != null) {
            textNotification.setText(result);
        } else {
            textNotification.setText("Không có kết quả.");
        }




    }
}