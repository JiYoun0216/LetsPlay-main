package com.example.letsplay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RoleplayingBackgroundActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private boolean isRecording = false;
    private boolean permissionToRecordAccepted = false;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private WAVRecorder wavRecorder;
    private File wavFile;

    private String serverURL_wss = "<serverURL>";
    private String serverURL_https = "<serverURL>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roleplaying);

        // 권한 요청
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        // RecyclerView 초기화
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // 초기 메시지 추가
        addGptMessage("안녕! 난 너의 친구야! 나랑 역할놀이 해볼까?");

        // 마이크 버튼 동작 설정
        ImageButton chatMic = findViewById(R.id.chat_mic);
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
        });

    }

    // 녹음 시작
    private void startRecording() throws IOException{
        File audioDir = getExternalFilesDir("audio");
        if (audioDir != null && !audioDir.exists()) audioDir.mkdirs();

        wavFile = new File(audioDir, "recording.wav");

        wavRecorder = new WAVRecorder(wavFile);
        wavRecorder.startRecording();

        isRecording = true;
        Toast.makeText(this, "녹음 시작", Toast.LENGTH_SHORT).show();
    }

    // 녹음 종료
    private void stopRecording() {
        wavRecorder.stopRecording();
        isRecording = false;
        Toast.makeText(this, "녹음 종료", Toast.LENGTH_SHORT).show();
    }

    private void processAudioFile() throws IOException{
        if (!wavFile.exists()) {
            Toast.makeText(this, "녹음 파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AudioWebSocketClient audioWebSocketClient = new AudioWebSocketClient(serverURL_wss);

        audioWebSocketClient.sendAudioData(wavFile);
    }

    // 사용자 메시지 추가
    private void addUserMessage(String message) {
        ChatMessage userMessage = new ChatMessage(message, true);
        chatMessages.add(userMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }

    // GPT 메시지 추가
    private void addGptMessage(String message) {
        ChatMessage gptMessage = new ChatMessage(message, false);
        chatMessages.add(gptMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        scrollToBottom();
    }

    // RecyclerView 스크롤 하단으로 이동
    private void scrollToBottom() {
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
    }

    // 권한 요청 결과 처리
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
