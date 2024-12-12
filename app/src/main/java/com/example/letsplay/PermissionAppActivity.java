package com.example.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class PermissionAppActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_app_activity);

        ImageButton checkButton2 = findViewById(R.id.checkButton2);
        checkButton2.setOnClickListener(v -> {
            Intent intent = new Intent(PermissionAppActivity.this, RewardsMainPageActivity.class);
            startActivity(intent);
        });
    }
}
