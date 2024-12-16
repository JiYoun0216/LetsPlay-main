package com.example.letsplay;

import android.graphics.Color;
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
import android.graphics.drawable.ColorDrawable;


import java.util.Objects;
import java.util.Random;

public class RewardsMainPageActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstatnceState) {
        super.onCreate(savedInstatnceState);
        setContentView(R.layout.rewards_main_page);

        ImageButton closeButton = findViewById(R.id.close_button);
        ImageButton firstButton = findViewById(R.id.first_button);
        ImageButton settingButton = findViewById(R.id.setting_button);
        ImageButton princessButton = findViewById(R.id.princess_button);
        ImageButton wizardButton = findViewById(R.id.wizard_button);
        ImageButton doctorButton = findViewById(R.id.doctor_button);
        ImageButton superheroButton = findViewById(R.id.superhero_button);
        ImageButton extraButton_1 = findViewById(R.id.extra_button_1);
        ImageButton extraButton_2 = findViewById(R.id.extra_button_2);

        closeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, EndingActivity.class);
            startActivity(intent);
        });

        settingButton.setOnClickListener(v -> {
            parent_verifyng_popup();
        });

        firstButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 역할놀이 해볼까?");
            startActivity(intent);
        });

        princessButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 공주놀이 해볼까?");
            startActivity(intent);
        });

        wizardButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 마법사놀이 해볼까?");
            startActivity(intent);
        });

        doctorButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 병원놀이 해볼까?");
            startActivity(intent);
        });

        superheroButton.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 슈퍼히어로 놀이 해볼까?");
            startActivity(intent);
        });

        extraButton_2.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 어떤 놀이 해볼까?");
            startActivity(intent);
        });

        extraButton_1.setOnClickListener(v -> {
            Intent intent = new Intent(RewardsMainPageActivity.this, RoleplayingBackgroundActivity.class);
            intent.putExtra("message", "안녕! 난 너의 친구야! 나랑 어떤 놀이 해볼까?");
            startActivity(intent);
        });
    }

// parent_verifing_popup

    private int correctAnswer; // 현재 정답을 저장하는 변수
private void parent_verifyng_popup() {
    Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.parents_quiz); // 기존 ParentsVerificationActivity XML 사용

    // 팝업 배경을 투명하게 설정
    if (dialog.getWindow() != null) {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

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

    ImageButton xButton = dialog.findViewById(R.id.x_button);
    xButton.setOnClickListener(v -> dialog.dismiss());

    dialog.show();
}

    // 흔들림 효과 추가
    private void shakeDialog(Dialog dialog) {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        View dialogView = Objects.requireNonNull(dialog.getWindow()).getDecorView();
        dialogView.startAnimation(shake);
    }

    private void showSettingPopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.setting_popup); // 팝업 레이아웃 설정
        dialog.setCancelable(false); // 외부 터치로 닫히지 않도록 설정

        // 팝업 배경을 투명하게 설정
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageButton xButton = dialog.findViewById(R.id.x_button);
        xButton.setOnClickListener(v -> dialog.dismiss()); // 다이얼로그 닫기

        dialog.show(); // 팝업 표시
    }
}
