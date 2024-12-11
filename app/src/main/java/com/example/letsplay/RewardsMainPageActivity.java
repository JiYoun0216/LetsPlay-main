package com.example.letsplay;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageButton;
import android.view.View;
import android.app.Dialog;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

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
            parent_verifyng_popup();
        });

        firstButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            startActivity(intent);
        });
    }

// parent_verifing_popup

    private int correctAnswer; // 현재 정답을 저장하는 변수
private void parent_verifyng_popup() {
    Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.parents_quiz); // 기존 ParentsVerificationActivity XML 사용

    TextView questionText = dialog.findViewById(R.id.question_text);
    TextView answerText = dialog.findViewById(R.id.answer_text);
    Button[] answerButtons = new Button[10];

    for (int i = 0; i <= 9; i++) {
        int buttonId = getResources().getIdentifier("answer_button_" + i, "id", getPackageName());
        answerButtons[i] = dialog.findViewById(buttonId);
    }

    // 문제 생성 및 정답 설정 함수
    Runnable updateQuestion = () -> {
        Random random = new Random();
        int num1 = random.nextInt(5) + 1; // 1~5
        int num2 = random.nextInt(5) + 1; // 1~5
        correctAnswer = num1 + num2; // 정답 계산
        questionText.setText(String.format("%d + %d = ", num1, num2));
        answerText.setText("?"); // '?'로 초기화
    };

    // 첫 문제 업데이트
    updateQuestion.run();

    // 버튼 클릭 이벤트 설정
    for (Button button : answerButtons) {
        button.setOnClickListener(v -> {
            int selectedAnswer = Integer.parseInt(button.getText().toString());
            answerText.setText(String.valueOf(selectedAnswer)); // '?'를 선택한 값으로 변경
            if (selectedAnswer == correctAnswer) {
                dialog.dismiss(); // 정답이면 팝업 닫기
                showSettingPopup(); // 설정 팝업 호출
            } else {
                shakeDialog(dialog); // 오답이면 흔들림 효과
                updateQuestion.run(); // 새로운 문제 생성
            }
        });
    }

    dialog.show(); // 팝업 표시
}

    // 흔들림 효과 추가
    private void shakeDialog(Dialog dialog) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        View dialogView = dialog.getWindow().getDecorView();
        dialogView.startAnimation(shake);
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
