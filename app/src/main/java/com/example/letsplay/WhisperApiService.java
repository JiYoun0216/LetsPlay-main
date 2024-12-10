package com.example.letsplay;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WhisperApiService {
    @Multipart
    @POST("/whisper/transcribe")
    Call<WhisperResponse> transcribe(@Part MultipartBody.Part file);
}
