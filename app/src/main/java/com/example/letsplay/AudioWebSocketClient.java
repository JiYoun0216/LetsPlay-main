package com.example.letsplay;

import okhttp3.*;
import okio.ByteString;

import org.json.JSONObject;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioWebSocketClient {
    private final OkHttpClient client;
    private WebSocket webSocket;

    public AudioWebSocketClient(String url) {
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS) // WebSocket 연결 유지
                .build();

        Request request = new Request.Builder().url(url).build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket Connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Message from server: " + text);
                try {
                    JSONObject response = new JSONObject(text);
                    String type = response.getString("type");
                    String content = response.getString("content");

                    switch (type) {
                        case "STT":
                            System.out.println("STT Result: " + content);
                            break;
                        case "LLM":
                            System.out.println("LLM Response: " + content);
                            break;
                        case "TTS":
                            System.out.println("TTS Response: " + content);
                            break;
                        case "END":
                            System.out.println("All processes completed. Closing WebSocket.");
                            close();
                            break;
                        default:
                            System.out.println("Unknown response type: " + type);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to parse response");
                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket Closing: " + reason);
                close();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("WebSocket Error: " + t.getMessage());
                close();
            }
        });
    }
    public void sendAudioData(File audioFile) throws IOException{
        byte[] audioData = readWavFileToByteArray(audioFile);
        sendAudioData(audioData);
    }
    public void sendAudioData(byte[] audioData) {
        if (webSocket != null) {
            webSocket.send(ByteString.of(audioData));
            System.out.println("Audio data sent: " + audioData.length + " bytes");
        } else {
            System.err.println("WebSocket is not connected. Data not sent.");
        }
    }
    public void close() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye!");
        }
    }

    public byte[] readWavFileToByteArray(File audioFile) throws IOException {
        if (!audioFile.exists() || !audioFile.isFile()) {
            throw new IOException("Invalid file: " + audioFile.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(audioFile)) {
            byte[] audioBytes = new byte[(int) audioFile.length()];
            int bytesRead = fis.read(audioBytes);
            if (bytesRead != audioBytes.length) {
                throw new IOException("Could not completely read the file: " + audioFile.getName());
            }
            return audioBytes;
        }
    }
}
