package com.example.letsplay;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class RoleplayingBackgroundActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roleplaying);

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoleplayingBackgroundActivity.this, MenuActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
        });
    }
}
