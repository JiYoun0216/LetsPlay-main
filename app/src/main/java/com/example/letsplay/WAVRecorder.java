package com.example.letsplay;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WAVRecorder {
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private File outputFile;

    private int sampleRate = 16000; // 16kHz
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO; // Mono
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT; // 16-bit PCM
    private int bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

    public WAVRecorder(File outputFile) throws SecurityException{
        this.outputFile = outputFile;
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                channelConfig,
                audioFormat,
                bufferSize
        );
    }

    public void startRecording() throws IOException {
        if (isRecording) return;

        isRecording = true;
        audioRecord.startRecording();

        // WAV 파일 작성
        new Thread(() -> {
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                // WAV 헤더 생성
                writeWavHeader(fileOutputStream, sampleRate, 1, 16);

                byte[] buffer = new byte[bufferSize];
                while (isRecording) {
                    int bytesRead = audioRecord.read(buffer, 0, buffer.length);
                    if (bytesRead > 0) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }

                // WAV 헤더 업데이트
                updateWavHeader(outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stopRecording() {
        if (!isRecording) return;

        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
    }

    // WAV 헤더 작성
    private void writeWavHeader(FileOutputStream out, int sampleRate, int channels, int bitsPerSample) throws IOException {
        byte[] header = new byte[44];
        long byteRate = sampleRate * channels * bitsPerSample / 8;

        header[0] = 'R'; // Chunk ID "RIFF"
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[8] = 'W'; // Format "WAVE"
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // Subchunk1 ID "fmt "
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // Subchunk1 Size (PCM)
        header[20] = 1; // Audio format (1 = PCM)
        header[22] = (byte) channels;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[32] = (byte) (channels * bitsPerSample / 8); // Block align
        header[34] = (byte) bitsPerSample; // Bits per sample
        header[36] = 'd'; // Subchunk2 ID "data"
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';

        out.write(header, 0, 44);
    }

    // WAV 헤더 업데이트
    private void updateWavHeader(File file) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            long fileSize = raf.length();
            raf.seek(4);
            raf.writeInt((int) (fileSize - 8)); // ChunkSize
            raf.seek(40);
            raf.writeInt((int) (fileSize - 44)); // Subchunk2Size
        }
    }
}
