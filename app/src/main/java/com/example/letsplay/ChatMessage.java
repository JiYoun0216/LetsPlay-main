package com.example.letsplay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private final String message;
    private final boolean isUser;
    private boolean isPending; // 점 애니메이션 플래그
    private final String timestamp; // 추가된 부분

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
        this.isPending = false;
        this.timestamp = getCurrentTime(); // 현재 시간 설정
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("a h:mm", Locale.getDefault()); // 오전/오후 h:mm 형식
        return sdf.format(new Date());
    }

}
