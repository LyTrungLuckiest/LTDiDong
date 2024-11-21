package com.example.btlon.Utils;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Khởi tạo Facebook SDK cho toàn bộ ứng dụng
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
