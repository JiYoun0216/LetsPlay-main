package com.example.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class EndingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ending);

        // 종료 버튼
        ImageButton endButton = findViewById(R.id.finish_button);
        endButton.setOnClickListener(v -> {
            Intent intent = new Intent(EndingActivity.this, SplashEndingActivity.class);
            startActivity(intent);
            finish(); // EndingActivity 종료
        });

        // 홈 버튼
        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(EndingActivity.this, RewardsMainPageActivity.class);
            startActivity(intent);
            finish(); // EndingActivity 종료
        });
    }
}
