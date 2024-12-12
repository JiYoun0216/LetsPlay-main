package com.example.letsplay;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhisperUploader {

    public void uploadAudioFile(File audioFile, WhisperCallback callback) {
        WhisperApiService service = ApiClient.getRetrofitInstance().create(WhisperApiService.class);

        MultipartBody.Part body = MultipartBody.Part.createFormData(
                "file", audioFile.getName(),
                RequestBody.create(MediaType.parse("audio/wav"), audioFile)
        );

        Call<WhisperResponse> call = service.transcribe(body);
        call.enqueue(new Callback<WhisperResponse>() {
            @Override
            public void onResponse(Call<WhisperResponse> call, Response<WhisperResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getText());
                } else {
                    callback.onError("Whisper API response failed");
                }
            }

            @Override
            public void onFailure(Call<WhisperResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}

