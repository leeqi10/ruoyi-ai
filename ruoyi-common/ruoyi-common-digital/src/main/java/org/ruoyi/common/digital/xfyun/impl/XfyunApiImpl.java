package org.ruoyi.common.digital.xfyun.impl;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.digital.xfyun.XfyunApi;
import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionRequest;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionResponse;
import org.ruoyi.common.digital.xfyun.websocket.XfyunWebSocketClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 科大讯飞API实现类
 * 仿照OpenAI的对接方式实现实时语音转写
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
public class XfyunApiImpl implements XfyunApi {

    private final XfyunConfig config;
    private volatile XfyunWebSocketClient currentClient;

    public XfyunApiImpl(XfyunConfig config) {
        this.config = config;
    }

    @Override
    public Single<XfyunWebSocketClient> realTimeTranscription(
            XfyunTranscriptionRequest request,
            Consumer<XfyunTranscriptionResponse> responseHandler,
            Consumer<String> errorHandler) {
        
        return Single.create(new SingleOnSubscribe<XfyunWebSocketClient>() {
            @Override
            public void subscribe(SingleEmitter<XfyunWebSocketClient> emitter) throws Exception {
                try {
                    // 创建WebSocket客户端
                    XfyunWebSocketClient client = new XfyunWebSocketClient(
                            buildConfig(request), 
                            responseHandler, 
                            errorHandler
                    );
                    
                    // 连接WebSocket
                    client.connect();
                    
                    // 等待认证完成
                    boolean authenticated = client.waitForAuthentication(30, TimeUnit.SECONDS);
                    if (!authenticated) {
                        emitter.onError(new RuntimeException("Authentication timeout"));
                        return;
                    }
                    
                    // 发送音频数据
                    if (request.getAudioData() != null && request.getAudioData().length > 0) {
                        client.sendAudioData(request.getAudioData());
                    }
                    
                    // 如果不是实时模式，发送结束信号
                    if (!request.getRealTime()) {
                        client.sendEndSignal();
                    }
                    
                    currentClient = client;
                    emitter.onSuccess(client);
                    
                } catch (Exception e) {
                    log.error("Failed to create real-time transcription client", e);
                    emitter.onError(e);
                }
            }
        });
    }

    @Override
    public Single<XfyunTranscriptionResponse> transcription(XfyunTranscriptionRequest request) {
        return Single.create(new SingleOnSubscribe<XfyunTranscriptionResponse>() {
            @Override
            public void subscribe(SingleEmitter<XfyunTranscriptionResponse> emitter) throws Exception {
                CompletableFuture<XfyunTranscriptionResponse> future = new CompletableFuture<>();
                StringBuilder fullText = new StringBuilder();
                
                try {
                    // 创建WebSocket客户端
                    XfyunWebSocketClient client = new XfyunWebSocketClient(
                            buildConfig(request),
                            response -> {
                                try {
                                    // 处理响应
                                    if (response.getData() != null && response.getData().getResult() != null) {
                                        XfyunTranscriptionResponse.TranscriptionResult result = response.getData().getResult();
                                        
                                        if (result.getWordSegments() != null) {
                                            for (XfyunTranscriptionResponse.WordSegment segment : result.getWordSegments()) {
                                                if (segment.getWordContents() != null) {
                                                    for (XfyunTranscriptionResponse.WordContent content : segment.getWordContents()) {
                                                        fullText.append(content.getWord());
                                                    }
                                                }
                                            }
                                        }
                                        
                                        // 如果是最终结果，完成Future
                                        if (Boolean.TRUE.equals(result.getIsFinal())) {
                                            XfyunTranscriptionResponse finalResponse = new XfyunTranscriptionResponse();
                                            finalResponse.setCode(0);
                                            finalResponse.setMessage("success");
                                            finalResponse.setSid(response.getSid());
                                            future.complete(finalResponse);
                                        }
                                    }
                                } catch (Exception e) {
                                    future.completeExceptionally(e);
                                }
                            },
                            error -> future.completeExceptionally(new RuntimeException(error))
                    );
                    
                    // 连接WebSocket
                    client.connect();
                    
                    // 等待认证完成
                    boolean authenticated = client.waitForAuthentication(30, TimeUnit.SECONDS);
                    if (!authenticated) {
                        emitter.onError(new RuntimeException("Authentication timeout"));
                        return;
                    }
                    
                    // 发送音频数据
                    if (request.getAudioData() != null && request.getAudioData().length > 0) {
                        client.sendAudioData(request.getAudioData());
                    }
                    
                    // 发送结束信号
                    client.sendEndSignal();
                    
                    // 等待结果
                    XfyunTranscriptionResponse response = future.get(60, TimeUnit.SECONDS);
                    emitter.onSuccess(response);
                    
                    // 关闭连接
                    client.close();
                    
                } catch (Exception e) {
                    log.error("Failed to perform transcription", e);
                    emitter.onError(e);
                }
            }
        });
    }

    @Override
    public Single<XfyunTranscriptionResponse> batchTranscription(XfyunTranscriptionRequest request) {
        // 批量处理与单个转录类似，但可以处理更大的音频文件
        // 这里简化处理，实际实现中可能需要分片处理
        return transcription(request);
    }

    @Override
    public Single<String[]> getSupportedAudioFormats() {
        return Single.fromCallable(() -> new String[]{
                "raw", "speex", "opus", "speex-wb", "opus-wb"
        });
    }

    @Override
    public Single<String[]> getSupportedLanguages() {
        return Single.fromCallable(() -> new String[]{
                "zh_cn", "en_us"
        });
    }

    @Override
    public Single<String[]> getSupportedDomains() {
        return Single.fromCallable(() -> new String[]{
                "iat", "medical", "finance", "education", "car", "smart_home"
        });
    }

    @Override
    public Single<Boolean> validateConfig(XfyunConfig config) {
        return Single.fromCallable(() -> {
            if (config == null) {
                return false;
            }
            
            if (config.getAppId() == null || config.getAppId().trim().isEmpty()) {
                return false;
            }
            
            if (config.getApiKey() == null || config.getApiKey().trim().isEmpty()) {
                return false;
            }
            
            return true;
        });
    }

    @Override
    public Single<Boolean> testConnection(XfyunConfig config) {
        return Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> emitter) throws Exception {
                try {
                    XfyunTranscriptionRequest testRequest = new XfyunTranscriptionRequest();
                    testRequest.setAppId(config.getAppId());
                    testRequest.setApiKey(config.getApiKey());
                    testRequest.setAudioData(new byte[0]); // 空音频数据用于测试
                    
                    XfyunWebSocketClient client = new XfyunWebSocketClient(
                            config,
                            response -> {
                                // 测试成功
                                emitter.onSuccess(true);
                            },
                            error -> {
                                // 测试失败
                                emitter.onSuccess(false);
                            }
                    );
                    
                    client.connect();
                    boolean authenticated = client.waitForAuthentication(10, TimeUnit.SECONDS);
                    client.close();
                    
                    emitter.onSuccess(authenticated);
                    
                } catch (Exception e) {
                    log.error("Connection test failed", e);
                    emitter.onSuccess(false);
                }
            }
        });
    }

    /**
     * 根据请求参数构建配置
     */
    private XfyunConfig buildConfig(XfyunTranscriptionRequest request) {
        XfyunConfig requestConfig = new XfyunConfig();
        requestConfig.setAppId(request.getAppId() != null ? request.getAppId() : config.getAppId());
        requestConfig.setApiKey(request.getApiKey() != null ? request.getApiKey() : config.getApiKey());
        requestConfig.setWsUrl(config.getWsUrl());
        requestConfig.setAudioFormat(request.getAudioFormat() != null ? request.getAudioFormat() : config.getAudioFormat());
        requestConfig.setSampleRate(request.getSampleRate() != null ? request.getSampleRate() : config.getSampleRate());
        requestConfig.setDataType(request.getDataType() != null ? request.getDataType() : config.getDataType());
        requestConfig.setLanguage(request.getLanguage() != null ? request.getLanguage() : config.getLanguage());
        requestConfig.setDomain(request.getDomain() != null ? request.getDomain() : config.getDomain());
        requestConfig.setPunctuation(request.getPunctuation() != null ? request.getPunctuation() : config.getPunctuation());
        requestConfig.setNumberConvert(request.getNumberConvert() != null ? request.getNumberConvert() : config.getNumberConvert());
        
        return requestConfig;
    }

    /**
     * 关闭当前连接
     */
    public void close() {
        if (currentClient != null && currentClient.isConnected()) {
            currentClient.close();
        }
    }
}
