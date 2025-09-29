package org.ruoyi.common.digital.xfyun.util;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 音频处理工具类
 * 用于处理音频文件的格式转换和数据提取
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
public class AudioUtils {

    /**
     * 将音频文件转换为PCM格式的字节数组
     *
     * @param inputStream 音频文件输入流
     * @param targetSampleRate 目标采样率
     * @param targetChannels 目标声道数
     * @return PCM格式的字节数组
     */
    public static byte[] convertToPcm(InputStream inputStream, float targetSampleRate, int targetChannels) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            
            // 获取原始音频格式
            AudioFormat originalFormat = audioInputStream.getFormat();
            log.info("Original audio format: {}", originalFormat);
            
            // 定义目标音频格式（PCM格式）
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    targetSampleRate,
                    16, // 16位
                    targetChannels,
                    targetChannels * 2, // 帧大小
                    targetSampleRate, // 帧率
                    false // 小端序
            );
            
            // 如果格式不匹配，进行转换
            if (!originalFormat.matches(targetFormat)) {
                audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                log.info("Converted to target format: {}", targetFormat);
            }
            
            // 读取音频数据
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            audioInputStream.close();
            outputStream.close();
            
            return outputStream.toByteArray();
            
        } catch (UnsupportedAudioFileException e) {
            log.error("Unsupported audio file format", e);
            throw new RuntimeException("Unsupported audio file format", e);
        } catch (IOException e) {
            log.error("Failed to read audio file", e);
            throw new RuntimeException("Failed to read audio file", e);
        }
    }

    /**
     * 将音频文件转换为16kHz单声道PCM格式
     * 这是科大讯飞实时语音转写API推荐的格式
     *
     * @param inputStream 音频文件输入流
     * @return 16kHz单声道PCM格式的字节数组
     */
    public static byte[] convertTo16kHzMonoPcm(InputStream inputStream) {
        return convertToPcm(inputStream, 16000.0f, 1);
    }

    /**
     * 将音频文件转换为8kHz单声道PCM格式
     * 适用于电话音质
     *
     * @param inputStream 音频文件输入流
     * @return 8kHz单声道PCM格式的字节数组
     */
    public static byte[] convertTo8kHzMonoPcm(InputStream inputStream) {
        return convertToPcm(inputStream, 8000.0f, 1);
    }

    /**
     * 将PCM数据分片，每片1280字节（40ms的16kHz音频数据）
     * 科大讯飞推荐每40ms发送1280字节的数据
     *
     * @param pcmData PCM音频数据
     * @param chunkSize 分片大小（默认1280字节）
     * @return 分片后的音频数据数组
     */
    public static byte[][] splitPcmData(byte[] pcmData, int chunkSize) {
        if (pcmData == null || pcmData.length == 0) {
            return new byte[0][];
        }
        
        int totalChunks = (pcmData.length + chunkSize - 1) / chunkSize;
        byte[][] chunks = new byte[totalChunks][];
        
        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, pcmData.length);
            int length = end - start;
            
            chunks[i] = new byte[length];
            System.arraycopy(pcmData, start, chunks[i], 0, length);
        }
        
        return chunks;
    }

    /**
     * 将PCM数据分片，每片1280字节（40ms的16kHz音频数据）
     *
     * @param pcmData PCM音频数据
     * @return 分片后的音频数据数组
     */
    public static byte[][] splitPcmData(byte[] pcmData) {
        return splitPcmData(pcmData, 1280);
    }

    /**
     * 获取音频时长（毫秒）
     *
     * @param pcmData PCM音频数据
     * @param sampleRate 采样率
     * @param channels 声道数
     * @param bitsPerSample 每样本位数
     * @return 音频时长（毫秒）
     */
    public static long getAudioDuration(byte[] pcmData, float sampleRate, int channels, int bitsPerSample) {
        if (pcmData == null || pcmData.length == 0) {
            return 0;
        }
        
        int bytesPerSample = bitsPerSample / 8;
        long totalSamples = pcmData.length / (channels * bytesPerSample);
        return (long) (totalSamples / sampleRate * 1000);
    }

    /**
     * 获取16位单声道PCM音频时长（毫秒）
     *
     * @param pcmData PCM音频数据
     * @param sampleRate 采样率
     * @return 音频时长（毫秒）
     */
    public static long getAudioDuration(byte[] pcmData, float sampleRate) {
        return getAudioDuration(pcmData, sampleRate, 1, 16);
    }
}
