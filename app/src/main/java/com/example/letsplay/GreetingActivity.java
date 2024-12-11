package com.example.letsplay;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;

public class GreetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_activity);

        // 3초 후 PrivacyPolicyActivity로 이동
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(GreetingActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
            finish(); // GreetingActivity 종료
        }, 3000); // 3000ms = 3초
    }
}
