package com.example.letsplay;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GptApiClient {

    @POST("/roleplay/process_audio") // GPT API의 엔드포인트
    Call<GptResponse> getResponse(@Body GptRequest request);
}
