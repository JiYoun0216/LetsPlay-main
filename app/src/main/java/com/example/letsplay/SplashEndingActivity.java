package com.example.letsplay;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashEndingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_ending);

        // 2초 후 앱 종료
        new Handler().postDelayed(() -> finishAffinity(), 2000);
    }
}
