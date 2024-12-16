package com.example.letsplay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoleplayingBackgroundActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    private boolean isRecording = false;

    private ImageButton chatMic;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private FrameLayout mike_touch_popup;
    private ImageView leftFrequency;
    private ImageView rightFrequency;


    private final Handler handler = new Handler();
    private boolean isAnimating = false;

    private WAVRecorder wavRecorder;
    private File wavFile;
    private String serverURL_wss = "wss://role-play-api-586976529959.asia-southeast1.run.app/roleplay/stream";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roleplaying);

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        if(message != null){
            addGptMessage(message);
        }

        // 권한 요청
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // RecyclerView 초기화
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // 메뉴 버튼 클릭 이벤트 추가
        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent menuIntent = new Intent(RoleplayingBackgroundActivity.this, MenuActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(menuIntent);
        });

        // View 초기화
        mike_touch_popup = findViewById(R.id.mike_touch_popup);
        leftFrequency = findViewById(R.id.leftFrequency);
        rightFrequency = findViewById(R.id.rightFrequency);


        // 4초 후 goodtext와 mike_touch_popup 표시
        handler.postDelayed(() -> {
            mike_touch_popup.setVisibility(View.VISIBLE);
        }, 4000);

        // 마이크 버튼 동작 설정
        chatMic = findViewById(R.id.chat_mic);
        chatMic.setOnClickListener(v -> {
            if (!permissionToRecordAccepted) {
                Toast.makeText(this, "녹음 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isRecording) {
                stopRecording();
                try {
                    processAudioFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("오디오 파일 전송 중");
                }
            }
            else {
                try {
                    startRecording();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("녹음 중 오류 발생");
                }
            }
            // 마이크 버튼 클릭 시 mike_touch_popup 숨기기
            mike_touch_popup.setVisibility(View.GONE);
        });
    }

    //녹음 시작
    private void startRecording() throws IOException{
        File audioDir = getExternalFilesDir("audio");
        if (audioDir != null && !audioDir.exists()) audioDir.mkdirs();

        wavFile = new File(audioDir, "recording.wav");

        wavRecorder = new WAVRecorder(wavFile);
        wavRecorder.startRecording();

        isRecording = true;
        isAnimating = true;
        startFrequencyAnimation();
        Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show();
    }

    //녹음 종료
    private void stopRecording() {
        if (wavRecorder != null) {
            wavRecorder.stopRecording();
        }
        isRecording = false;
        isAnimating = false;
        stopFrequencyAnimation();
        Toast.makeText(this, "녹음 종료", Toast.LENGTH_SHORT).show();
    }

    private void processAudioFile() throws IOException{
        if (!wavFile.exists()) {
            Toast.makeText(this, "녹음 파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AudioWebSocketClient audioWebSocketClient = new AudioWebSocketClient(this, serverURL_wss, this) {
            @Override
            public void onMessage(String type, String content) {
//                runOnUiThread(() -> {
//                    if ("LLM".equals(type)) {
//                        addGptMessage(content); // LLM Response 추가
//                    } else if ("STT".equals(type)) {
//                        addUserMessage(content); // STT Result 추가
//                    }
//                });
            }
        };
        audioWebSocketClient.sendAudioData(wavFile);
    }

    //주파수 애니메이션
    private void startFrequencyAnimation() {
        new Thread(() -> {
            int scale = 1;
            boolean increasing = true;

            while (isAnimating) {
                int finalScale = scale;
                runOnUiThread(() -> {
                    leftFrequency.setScaleY(finalScale * 1.3f);
                    rightFrequency.setScaleY(finalScale * 1.3f);
                });

                if (increasing) {
                    scale++;
                    if (scale >= 10) increasing = false;
                } else {
                    scale--;
                    if (scale <= 1) increasing = true;
                }

                try {
                    Thread.sleep(100); // 애니메이션 속도
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void stopFrequencyAnimation() {
        runOnUiThread(() -> {
            leftFrequency.setScaleY(1);
            rightFrequency.setScaleY(1);
        });
    }

    // 사용자 메시지 추가
    public void addUserMessage(String message) {
        ChatMessage userMessage = new ChatMessage(message, true);
        userMessage.setPending(true);
        chatMessages.add(userMessage);

        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();

        simulateTyping(userMessage);
    }

    private void simulateTyping(ChatMessage userMessage) {
        // 1. 3초 후 GPT 응답 시뮬레이션
        handler.postDelayed(() -> {
            // 사용자 메시지의 isPending 상태 해제
            userMessage.setPending(false);
            chatAdapter.notifyItemChanged(chatMessages.indexOf(userMessage));
        }, 3000); // 3초 동안 타이핑 애니메이션
    }

    // GPT 메시지 추가 (딜레이 포함)
    public void addGptMessage(String message) {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        handler.postDelayed(() -> {
            ChatMessage gptMessage = new ChatMessage(message, false);

            chatMessages.add(gptMessage);

            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            scrollToBottom();
        }, 3000); // 3초 딜레이
    }


    private void scrollToBottom() {
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permissionToRecordAccepted) {
                Toast.makeText(this, "녹음 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
