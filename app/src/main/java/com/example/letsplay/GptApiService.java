package com.example.letsplay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GptApiService {

    public void getGptResponse(String transcription, GptCallback callback) {
        GptApiClient service = ApiClient.getRetrofitInstance().create(GptApiClient.class);

        GptRequest request = new GptRequest(transcription);
        Call<GptResponse> call = service.getResponse(request);

        call.enqueue(new Callback<GptResponse>() {
            @Override
            public void onResponse(Call<GptResponse> call, Response<GptResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getReply());
                } else {
                    callback.onError("GPT API response failed");
                }
            }

            @Override
            public void onFailure(Call<GptResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}

