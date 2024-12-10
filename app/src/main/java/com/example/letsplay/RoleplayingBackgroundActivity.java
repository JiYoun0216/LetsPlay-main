package com.example.letsplay;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoleplayingBackgroundActivity extends AppCompatActivity{

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private String audioFilePath;

    private final Handler handler = new Handler();

    private ImageView goodtext;
    private FrameLayout mike_touch_popup;
    private ImageView leftFrequency;
    private ImageView rightFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roleplaying);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        goodtext = findViewById(R.id.goodtext);
        mike_touch_popup = findViewById((R.id.mike_touch_popup));

        handler.postDelayed(() -> {
            goodtext.setVisibility(View.VISIBLE);
            mike_touch_popup.setVisibility((View.VISIBLE));
        }, 4000); // 4초 후 이미지 표시

        // 초기 메시지
        addGptMessage("안녕! 난 너의 친구야! 나랑 역할놀이 해볼까?");


        // 녹음 버튼
        ImageButton chatMic = findViewById(R.id.chat_mic);
        chatMic.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
                processAudioFile(audioFilePath); // 녹음 종료 후 파일 처리
            } else {
                startRecording();
            }
        });

        // 주파수 애니메이션 뷰 연결
        leftFrequency = findViewById(R.id.leftFrequency);
        rightFrequency = findViewById(R.id.rightFrequency);

        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(RoleplayingBackgroundActivity.this, MenuActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
        });
    }

    // 음성 녹음 시작
    private void startRecording() {
        try {
            File audioDir = getExternalFilesDir("audio");
            if (!audioDir.exists()) audioDir.mkdirs();


            File audioFile = new File(audioDir, "recording.wav");
            audioFilePath = audioFile.getAbsolutePath();

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            startFrequencyAnimation();

            isRecording = true;
            Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "녹음 시작 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // 음성 녹음 종료
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            isRecording = false;
            stopFrequencyAnimation();

            Toast.makeText(this, "녹음 종료", Toast.LENGTH_SHORT).show();
        }
    }

    // 주파수 애니메이션 시작
    private void startFrequencyAnimation() {
        new Thread(() -> {
            boolean increasing = true;
            int scale = 1;
            while (isRecording) {
                int finalScale = scale;
                runOnUiThread(() -> {
                    leftFrequency.setScaleY(finalScale);
                    rightFrequency.setScaleY(finalScale);
                });
                if (increasing) {
                    scale++;
                    if (scale > 10) increasing = false;
                } else {
                    scale--;
                    if (scale < 1) increasing = true;
                }
                try {
                    Thread.sleep(100); // 애니메이션 속도 조정
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopFrequencyAnimation() {
        leftFrequency.setScaleY(1);
        rightFrequency.setScaleY(1);
    }

    private void processAudioFile(String filePath) {
        File audioFile = new File(filePath);

        // Whisper API 호출
        WhisperUploader whisperUploader = new WhisperUploader();
        whisperUploader.uploadAudioFile(audioFile, new WhisperCallback() {
            @Override
            public void onSuccess(String transcription) {
                // Whisper 전사 결과를 사용자 말풍선으로 추가
                addUserMessage(transcription);

                // GPT API 호출
                GptApiService gptApiService = new GptApiService();
                gptApiService.getGptResponse(transcription, new GptCallback() {
                    @Override
                    public void onSuccess(String response) {
                        // GPT 응답을 GPT 말풍선으로 추가
                        addUserMessage(response);
                    }
                    @Override
                    public void onError(String errorMessage) {
                        // GPT API 응답 실패 처리
                        Toast.makeText(RoleplayingBackgroundActivity.this, "GPT Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String errorMessage) {
                // Whisper API 응답 실패 처리
                Toast.makeText(RoleplayingBackgroundActivity.this, "Whisper Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserMessage(String message) {
        ChatMessage userMessage = new ChatMessage(message, true);
        chatMessages.add(userMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }

    private void addGptMessage(String message) {
        ChatMessage gptMessage = new ChatMessage(message, false);
        chatMessages.add(gptMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        RecyclerView chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

}
