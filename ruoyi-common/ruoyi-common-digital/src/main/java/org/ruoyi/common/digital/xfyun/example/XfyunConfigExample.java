package org.ruoyi.common.digital.xfyun.example;

import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.digital.config.properties.XfyunProperties;
import org.ruoyi.common.digital.xfyun.XfyunApi;
import org.ruoyi.common.digital.xfyun.XfyunClient;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionRequest;
import org.ruoyi.common.digital.xfyun.util.AudioUtils;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 科大讯飞配置使用示例
 * 展示如何使用Spring Boot配置和Builder模式
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
public class XfyunConfigExample {

    /**
     * 使用Spring Boot配置的示例
     */
    public void springBootConfigExample(XfyunProperties properties, XfyunApi xfyunApi) {
        log.info("=== 使用Spring Boot配置的示例 ===");
        
        try {
            // 准备音频数据
            byte[] audioData = loadAudioData("path/to/audio.wav");
            
            // 创建请求，使用配置中的默认值
            XfyunTranscriptionRequest request = new XfyunTranscriptionRequest();
            request.setAudioData(audioData);
            request.setLanguage(properties.getLanguage().getDefaultLanguage());
            request.setDomain(properties.getLanguage().getDefaultDomain());
            request.setAudioFormat(properties.getAudio().getFormat());
            request.setSampleRate(properties.getAudio().getSampleRate());
            
            // 执行语音转写
            xfyunApi.transcription(request).subscribe(
                    response -> {
                        log.info("Spring Boot配置转写完成: {}", response);
                    },
                    error -> {
                        log.error("Spring Boot配置转写失败", error);
                    }
            );
            
        } catch (Exception e) {
            log.error("Spring Boot配置示例失败", e);
        }
    }

    /**
     * 使用Builder模式的示例
     */
    public void builderPatternExample() {
        log.info("=== 使用Builder模式的示例 ===");
        
        try {
            // 使用Builder模式创建客户端
            XfyunClient client = XfyunClient.builder()
                    .appId("your_app_id")
                    .apiKey("your_api_key")
                    .wsUrl("wss://rtasr.xfyun.cn/v1/ws")
                    .connectTimeout(30000)
                    .readTimeout(60000)
                    .retryCount(3)
                    .audioFormat("raw")
                    .sampleRate("16000")
                    .language("zh_cn")
                    .domain("iat")
                    .punctuation("1")
                    .numberConvert("zh")
                    .build();
            
            // 准备音频数据
            byte[] audioData = loadAudioData("path/to/audio.wav");
            
            // 创建请求
            XfyunTranscriptionRequest request = new XfyunTranscriptionRequest();
            request.setAudioData(audioData);
            request.setRealTime(false);
            
            // 执行语音转写
            client.getXfyunApi().transcription(request).subscribe(
                    response -> {
                        log.info("Builder模式转写完成: {}", response);
                    },
                    error -> {
                        log.error("Builder模式转写失败", error);
                    }
            );
            
        } catch (Exception e) {
            log.error("Builder模式示例失败", e);
        }
    }

    /**
     * 使用WebSocket客户端的示例
     */
    public void webSocketClientExample() {
        log.info("=== 使用WebSocket客户端的示例 ===");
        
        try {
            // 创建客户端
            XfyunClient client = XfyunClient.builder()
                    .appId("your_app_id")
                    .apiKey("your_api_key")
                    .build();
            
            // 准备音频数据
            byte[] audioData = loadAudioData("path/to/audio.wav");
            
            // 创建WebSocket客户端
            var websocketClient = client.createWebSocketClient(
                    response -> {
                        // 处理实时转写结果
                        log.info("收到实时转写结果: {}", response);
                        
                        if (response.getData() != null && response.getData().getResult() != null) {
                            var result = response.getData().getResult();
                            
                            if (result.getWordSegments() != null) {
                                StringBuilder text = new StringBuilder();
                                for (var segment : result.getWordSegments()) {
                                    if (segment.getWordContents() != null) {
                                        for (var content : segment.getWordContents()) {
                                            text.append(content.getWord());
                                        }
                                    }
                                }
                                
                                if (Boolean.TRUE.equals(result.getIsFinal())) {
                                    log.info("最终转写结果: {}", text.toString());
                                } else {
                                    log.info("中间转写结果: {}", text.toString());
                                }
                            }
                        }
                    },
                    error -> {
                        // 处理错误
                        log.error("WebSocket转写错误: {}", error);
                    }
            );
            
            // 连接WebSocket
            websocketClient.connect();
            
            // 等待认证完成
            boolean authenticated = websocketClient.waitForAuthentication(30, java.util.concurrent.TimeUnit.SECONDS);
            if (!authenticated) {
                log.error("WebSocket认证失败");
                return;
            }
            
            // 分片发送音频数据
            byte[][] chunks = AudioUtils.splitPcmData(audioData, 1280);
            for (byte[] chunk : chunks) {
                websocketClient.sendAudioData(chunk);
                try {
                    Thread.sleep(40); // 每40ms发送一次
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            // 发送结束信号
            websocketClient.sendEndSignal();
            
            // 等待一段时间后关闭连接
            Thread.sleep(5000);
            websocketClient.close();
            
        } catch (Exception e) {
            log.error("WebSocket客户端示例失败", e);
        }
    }

    /**
     * 配置验证示例
     */
    public void configValidationExample(XfyunApi xfyunApi) {
        log.info("=== 配置验证示例 ===");
        
        // 验证配置
        xfyunApi.validateConfig(null).subscribe(
                isValid -> {
                    log.info("配置验证结果: {}", isValid);
                },
                error -> {
                    log.error("配置验证失败", error);
                }
        );
        
        // 测试连接
        xfyunApi.testConnection(null).subscribe(
                isConnected -> {
                    log.info("连接测试结果: {}", isConnected);
                },
                error -> {
                    log.error("连接测试失败", error);
                }
        );
    }

    /**
     * 加载音频数据
     */
    private byte[] loadAudioData(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            // 将音频文件转换为16kHz单声道PCM格式
            return AudioUtils.convertTo16kHzMonoPcm(inputStream);
        } catch (Exception e) {
            log.error("加载音频数据失败: {}", filePath, e);
            throw new RuntimeException("加载音频数据失败", e);
        }
    }
}
