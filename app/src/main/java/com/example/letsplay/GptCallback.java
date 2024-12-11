package com.example.letsplay;

public interface GptCallback {
    // GPT API 호출 성공 시 호출
    void onSuccess(String response);

    // GPT API 호출 실패 시 호출
    void onError(String errorMessage);
}
