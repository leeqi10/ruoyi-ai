package org.ruoyi.common.digital.xfyun.example;

import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.digital.xfyun.XfyunApi;
import org.ruoyi.common.digital.xfyun.XfyunApiFactory;
import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionRequest;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionResponse;
import org.ruoyi.common.digital.xfyun.util.AudioUtils;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 科大讯飞API使用示例
 * 仿照OpenAI的使用方式演示如何调用科大讯飞实时语音转写API
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
public class XfyunApiExample {

    public static void main(String[] args) {
        // 配置参数
        String appId = "your_app_id";
        String apiKey = "your_api_key";
        
        // 创建API实例
        XfyunApi xfyunApi = XfyunApiFactory.create(appId, apiKey);
        
        // 示例1: 实时语音转写（流式处理）
        realTimeTranscriptionExample(xfyunApi);
        
        // 示例2: 同步语音转写
        syncTranscriptionExample(xfyunApi);
        
        // 示例3: 批量语音转写
        batchTranscriptionExample(xfyunApi);
        
        // 示例4: 获取支持的格式
        getSupportedFormatsExample(xfyunApi);
        
        // 示例5: 配置验证和连接测试
        configValidationExample(xfyunApi);
    }

    /**
     * 实时语音转写示例（流式处理）
     * 类似于OpenAI的流式聊天完成
     */
    private static void realTimeTranscriptionExample(XfyunApi xfyunApi) {
        log.info("=== 实时语音转写示例（流式处理） ===");
        
        try {
            // 准备音频数据
            byte[] audioData = loadAudioData("path/to/audio.wav");
            
            // 创建请求
            XfyunTranscriptionRequest request = new XfyunTranscriptionRequest();
            request.setAudioData(audioData);
            request.setRealTime(true);
            request.setLanguage("zh_cn");
            request.setDomain("iat");
            
            // 执行实时转写
            xfyunApi.realTimeTranscription(
                    request,
                    response -> {
                        // 处理响应
                        log.info("收到转写结果: {}", response);
                        
                        if (response.getData() != null && response.getData().getResult() != null) {
                            XfyunTranscriptionResponse.TranscriptionResult result = response.getData().getResult();
                            
                            if (result.getWordSegments() != null) {
                                StringBuilder text = new StringBuilder();
                                for (XfyunTranscriptionResponse.WordSegment segment : result.getWordSegments()) {
                                    if (segment.getWordContents() != null) {
                                        for (XfyunTranscriptionResponse.WordContent content : segment.getWordContents()) {
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
                        log.error("转写错误: {}", error);
                    }
            ).subscribe(
                    client -> {
                        log.info("WebSocket客户端创建成功");
                        
                        // 如果需要持续发送音频数据，可以在这里实现
                        // 例如：分片发送音频数据
                        byte[][] chunks = AudioUtils.splitPcmData(request.getAudioData());
                        for (byte[] chunk : chunks) {
                            client.sendAudioData(chunk);
                            try {
                                Thread.sleep(40); // 每40ms发送一次
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        }
                        
                        // 发送结束信号
                        client.sendEndSignal();
                    },
                    error -> {
                        log.error("创建WebSocket客户端失败", error);
                    }
            );
            
        } catch (Exception e) {
            log.error("实时语音转写失败", e);
        }
    }

    /**
     * 同步语音转写示例
     * 类似于OpenAI的同步聊天完成
     */
    private static void syncTranscriptionExample(XfyunApi xfyunApi) {
        log.info("=== 同步语音转写示例 ===");
        
        try {
            // 准备音频数据
            byte[] audioData = loadAudioData("path/to/audio.wav");
            
            // 创建请求
            XfyunTranscriptionRequest request = new XfyunTranscriptionRequest();
            request.setAudioData(audioData);
            request.setRealTime(false);
            request.setLanguage("zh_cn");
            request.setDomain("iat");
            
            // 执行同步转写
            xfyunApi.transcription(request).subscribe(
                    response -> {
                        log.info("同步转写完成: {}", response);
                        
                        if (response.getData() != null && response.getData().getResult() != null) {
                            XfyunTranscriptionResponse.TranscriptionResult result = response.getData().getResult();
                            
                            if (result.getWordSegments() != null) {
                                StringBuilder text = new StringBuilder();
                                for (XfyunTranscriptionResponse.WordSegment segment : result.getWordSegments()) {
                                    if (segment.getWordContents() != null) {
                                        for (XfyunTranscriptionResponse.WordContent content : segment.getWordContents()) {
                                            text.append(content.getWord());
                                        }
                                    }
                                }
                                
                                log.info("转写结果: {}", text.toString());
                            }
                        }
                    },
                    error -> {
                        log.error("同步语音转写失败", error);
                    }
            );
            
        } catch (Exception e) {
            log.error("同步语音转写失败", e);
        }
    }

    /**
     * 批量语音转写示例
     * 类似于OpenAI的批量处理
     */
    private static void batchTranscriptionExample(XfyunApi xfyunApi) {
        log.info("=== 批量语音转写示例 ===");
        
        try {
            // 准备音频数据
            byte[] audioData = loadAudioData("path/to/large_audio.wav");
            
            // 创建请求
            XfyunTranscriptionRequest request = new XfyunTranscriptionRequest();
            request.setAudioData(audioData);
            request.setRealTime(false);
            request.setLanguage("zh_cn");
            request.setDomain("iat");
            
            // 执行批量转写
            xfyunApi.batchTranscription(request).subscribe(
                    response -> {
                        log.info("批量转写完成: {}", response);
                    },
                    error -> {
                        log.error("批量语音转写失败", error);
                    }
            );
            
        } catch (Exception e) {
            log.error("批量语音转写失败", e);
        }
    }

    /**
     * 获取支持的格式示例
     * 类似于OpenAI的模型列表
     */
    private static void getSupportedFormatsExample(XfyunApi xfyunApi) {
        log.info("=== 获取支持的格式示例 ===");
        
        // 获取支持的音频格式
        xfyunApi.getSupportedAudioFormats().subscribe(
                formats -> {
                    log.info("支持的音频格式: {}", String.join(", ", formats));
                },
                error -> {
                    log.error("获取支持的音频格式失败", error);
                }
        );
        
        // 获取支持的语言
        xfyunApi.getSupportedLanguages().subscribe(
                languages -> {
                    log.info("支持的语言: {}", String.join(", ", languages));
                },
                error -> {
                    log.error("获取支持的语言失败", error);
                }
        );
        
        // 获取支持的领域
        xfyunApi.getSupportedDomains().subscribe(
                domains -> {
                    log.info("支持的领域: {}", String.join(", ", domains));
                },
                error -> {
                    log.error("获取支持的领域失败", error);
                }
        );
    }

    /**
     * 配置验证和连接测试示例
     * 类似于OpenAI的API密钥验证
     */
    private static void configValidationExample(XfyunApi xfyunApi) {
        log.info("=== 配置验证和连接测试示例 ===");
        
        // 创建配置
        XfyunConfig config = new XfyunConfig();
        config.setAppId("your_app_id");
        config.setApiKey("your_api_key");
        
        // 验证配置
        xfyunApi.validateConfig(config).subscribe(
                isValid -> {
                    log.info("配置验证结果: {}", isValid);
                },
                error -> {
                    log.error("配置验证失败", error);
                }
        );
        
        // 测试连接
        xfyunApi.testConnection(config).subscribe(
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
    private static byte[] loadAudioData(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            // 将音频文件转换为16kHz单声道PCM格式
            return AudioUtils.convertTo16kHzMonoPcm(inputStream);
        } catch (Exception e) {
            log.error("加载音频数据失败: {}", filePath, e);
            throw new RuntimeException("加载音频数据失败", e);
        }
    }
}
