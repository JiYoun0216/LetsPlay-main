package com.example.letsplay;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;
import android.view.View;
import android.app.Dialog;

public class RewardsMainPageActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstatnceState) {
        super.onCreate(savedInstatnceState);
        setContentView(R.layout.rewards_main_page);

        ImageButton closeButton = findViewById(R.id.close_button);
        ImageButton firstButton = findViewById(R.id.first_button);
        ImageButton settingButton = findViewById(R.id.setting_button);

        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, EndingActivity.class);
            startActivity(intent);
        });
        settingButton.setOnClickListener(v -> {
            showSettingPopup();
        });

        firstButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            startActivity(intent);
        });
    }

    private void showSettingPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.setting_popup); // 팝업 레이아웃 설정
        dialog.setCancelable(false); // 외부 터치로 닫히지 않도록 설정

        ImageButton xButton = dialog.findViewById(R.id.x_button);
        xButton.setOnClickListener(v -> dialog.dismiss()); // 다이얼로그 닫기

        dialog.show(); // 팝업 표시
    }


}
