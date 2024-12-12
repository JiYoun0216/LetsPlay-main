package com.example.letsplay;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import okhttp3.*;
import okio.ByteString;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public abstract class AudioWebSocketClient {
    private WebSocket webSocket;
    private final Context context;
    private AudioPlayer audioPlayer;
    private RoleplayingBackgroundActivity activity;

    public AudioWebSocketClient(Context context, String url, RoleplayingBackgroundActivity activity) {
        this.audioPlayer = new AudioPlayer();
        this.context = context;
        this.activity = activity;
        // WebSocket 연결 유지
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS) // WebSocket 연결 유지
                .build();
        Request request = new Request.Builder().url(url).build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket Connected");
                audioPlayer.start();
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
                            activity.runOnUiThread(() ->
                                    activity.addUserMessage(content));
                            break;
                        case "LLM":
                            System.out.println("LLM Response: " + content);
                            activity.runOnUiThread(() ->
                                    activity.addGptMessage(content));
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
            public void onMessage(WebSocket webSocket, ByteString bytes) {
//                System.out.println("Received PCM data: " + bytes.size() + " bytes");

                // Add PCM data to playback queue
                audioPlayer.addAudioChunk(bytes.toByteArray());
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

    private void savePcmAsWav(File wavFile, byte[] pcmData, int sampleRate, int channels, int bitsPerSample) {
        try (FileOutputStream fos = new FileOutputStream(wavFile)) {
            // WAV 헤더 작성
            writeWavHeader(fos, pcmData.length, sampleRate, channels, bitsPerSample);

            // PCM 데이터 추가
            fos.write(pcmData);

            Log.d("AudioProcessing", "WAV file saved: " + wavFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e("AudioProcessing", "Failed to save WAV file", e);
        }
    }

    private void writeWavHeader(FileOutputStream out, int pcmDataLength, int sampleRate, int channels, int bitsPerSample) throws IOException {
        long totalDataLength = pcmDataLength + 36;
        long byteRate = sampleRate * channels * bitsPerSample / 8;

        byte[] header = new byte[44];

        header[0] = 'R'; // Chunk ID
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLength & 0xff); // Chunk Size
        header[5] = (byte) ((totalDataLength >> 8) & 0xff);
        header[6] = (byte) ((totalDataLength >> 16) & 0xff);
        header[7] = (byte) ((totalDataLength >> 24) & 0xff);
        header[8] = 'W'; // Format
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // Subchunk1 ID
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // Subchunk1 Size
        header[20] = 1; // Audio Format (1 = PCM)
        header[22] = (byte) channels; // Number of channels
        header[24] = (byte) (sampleRate & 0xff); // Sample rate
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff); // Byte rate
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (channels * bitsPerSample / 8); // Block align
        header[34] = (byte) bitsPerSample; // Bits per sample
        header[36] = 'd'; // Subchunk2 ID
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (pcmDataLength & 0xff); // Subchunk2 Size
        header[41] = (byte) ((pcmDataLength >> 8) & 0xff);
        header[42] = (byte) ((pcmDataLength >> 16) & 0xff);
        header[43] = (byte) ((pcmDataLength >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    public abstract void onMessage(String type, String content);
}
