package com.example.letsplay;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AudioPlayer {
    private static final String TAG = "AudioPlayer";
    private static final int SAMPLE_RATE = 24000; // 24kHz
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int MIN_BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioTrack audioTrack;
    private BlockingQueue<byte[]> audioQueue = new LinkedBlockingQueue<>(200); // Large queue size
    private boolean isPlaying = false;

    public AudioPlayer() {
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                MIN_BUFFER_SIZE,
                AudioTrack.MODE_STREAM
        );
    }

    public void start() {
        if (isPlaying) return;

        isPlaying = true;
        audioTrack.play();

        new Thread(() -> {
            try {
                while (isPlaying) {
                    byte[] chunk = audioQueue.take(); // Blocking until data is available
                    audioTrack.write(chunk, 0, chunk.length); // Write data to AudioTrack
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Playback interrupted", e);
            }
        }).start();
    }

    public void stop() {
        isPlaying = false;
        audioTrack.stop();
        audioTrack.release();
    }

    public void addAudioChunk(byte[] chunk) {
        try {
            // Blocking if the queue is full
            audioQueue.put(chunk);
        } catch (InterruptedException e) {
            Log.e(TAG, "Failed to add audio chunk to queue", e);
        }
    }

    public boolean isQueueFull() {
        return audioQueue.remainingCapacity() == 0;
    }
}
