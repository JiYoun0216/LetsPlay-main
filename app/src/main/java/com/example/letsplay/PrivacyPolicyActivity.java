package com.example.letsplay;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private boolean isArea1Clicked = false;
    private boolean isArea2Clicked = false;
    private boolean isArea3Clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy_activity);

        ImageView clickArea1 = findViewById(R.id.clickArea1);
        ImageView clickArea2 = findViewById(R.id.clickArea2);
        ImageView clickArea3 = findViewById(R.id.clickArea3);
        ImageButton checkButton = findViewById(R.id.checkButton);

        checkButton.setEnabled(false);

        clickArea1.setOnClickListener(v -> {
            isArea1Clicked = !isArea1Clicked;
            updateImage(clickArea1, isArea1Clicked);
            checkAllAreasClicked(checkButton);
        });

        clickArea2.setOnClickListener(v -> {
            isArea2Clicked = !isArea2Clicked;
            updateImage(clickArea2, isArea2Clicked);
            checkAllAreasClicked(checkButton);
        });

        clickArea3.setOnClickListener(v -> {
            isArea3Clicked = !isArea3Clicked;
            updateImage(clickArea3, isArea3Clicked);
            checkAllAreasClicked(checkButton);
        });

        checkButton.setOnClickListener(v -> {
            Intent intent = new Intent(PrivacyPolicyActivity.this, PermissionAppActivity.class);
            startActivity(intent);
        });
    }

    private void updateImage(ImageView imageView, boolean isClicked) {
        if (isClicked) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setBackgroundResource(R.drawable.checked_image); // 이미지 표시
        } else {
            imageView.setBackgroundResource(0); // 이미지 제거
        }
    }

    private void checkAllAreasClicked(ImageButton checkButton) {
        if (isArea1Clicked && isArea2Clicked && isArea3Clicked) {
            checkButton.setEnabled(true); // 버튼 활성화
        } else {
            checkButton.setEnabled(false); // 버튼 비활성화
        }
    }
}
