package org.ruoyi.common.digital.xfyun.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.ruoyi.common.digital.xfyun.entity.XfyunAuthRequest;
import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionResponse;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 科大讯飞WebSocket客户端
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
public class XfyunWebSocketClient extends WebSocketClient {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Consumer<XfyunTranscriptionResponse> responseHandler;
    private final Consumer<String> errorHandler;
    private CountDownLatch authLatch;
    private volatile boolean authenticated = false;
    
    public XfyunWebSocketClient(XfyunConfig config, 
                               Consumer<XfyunTranscriptionResponse> responseHandler,
                               Consumer<String> errorHandler) {
        super(buildUri(config));
        this.responseHandler = responseHandler;
        this.errorHandler = errorHandler;
    }
    
    private static URI buildUri(XfyunConfig config) {
        try {
            XfyunAuthRequest authRequest = XfyunAuthRequest.builder()
                    .appId(config.getAppId())
                    .timestamp(String.valueOf(System.currentTimeMillis() / 1000))
                    .signature(generateSignature(config))
                    .audioFormat(config.getAudioFormat())
                    .sampleRate(config.getSampleRate())
                    .dataType(config.getDataType())
                    .language(config.getLanguage())
                    .domain(config.getDomain())
                    .punctuation(config.getPunctuation())
                    .numberConvert(config.getNumberConvert())
                    .build();
            
            String queryString = buildQueryString(authRequest);
            return new URI(config.getWsUrl() + "?" + queryString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build WebSocket URI", e);
        }
    }
    
    private static String generateSignature(XfyunConfig config) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
            String baseString = config.getAppId() + timestamp;
            
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            md5.update(baseString.getBytes("UTF-8"));
            String md5Base = bytesToHex(md5.digest());
            
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
            mac.init(new javax.crypto.spec.SecretKeySpec(config.getApiKey().getBytes("UTF-8"), "HmacSHA1"));
            byte[] signature = mac.doFinal(md5Base.getBytes("UTF-8"));
            
            return java.util.Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate signature", e);
        }
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
    
    private static String buildQueryString(XfyunAuthRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("appid=").append(request.getAppId());
        sb.append("&ts=").append(request.getTimestamp());
        sb.append("&signa=").append(request.getSignature());
        sb.append("&aue=").append(request.getAudioFormat());
        sb.append("&sample_rate=").append(request.getSampleRate());
        sb.append("&data_type=").append(request.getDataType());
        sb.append("&language=").append(request.getLanguage());
        sb.append("&domain=").append(request.getDomain());
        sb.append("&pd=").append(request.getPunctuation());
        sb.append("&rlang=").append(request.getNumberConvert());
        return sb.toString();
    }
    
    @Override
    public void onOpen(ServerHandshake handshake) {
        log.info("WebSocket connection opened");
        this.authLatch = new CountDownLatch(1);
        authenticated = true;
        authLatch.countDown();
    }
    
    @Override
    public void onMessage(String message) {
        try {
            log.debug("Received message: {}", message);
            XfyunTranscriptionResponse response = objectMapper.readValue(message, XfyunTranscriptionResponse.class);
            
            if (response.getCode() != null && response.getCode() != 0) {
                errorHandler.accept("API Error: " + response.getMessage());
                return;
            }
            
            responseHandler.accept(response);
        } catch (Exception e) {
            log.error("Failed to parse response message", e);
            errorHandler.accept("Failed to parse response: " + e.getMessage());
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("WebSocket connection closed: {} - {}", code, reason);
        authenticated = false;
    }
    
    @Override
    public void onError(Exception ex) {
        log.error("WebSocket error", ex);
        authenticated = false;
        errorHandler.accept("WebSocket error: " + ex.getMessage());
    }
    
    /**
     * 等待认证完成
     */
    public boolean waitForAuthentication(long timeout, TimeUnit unit) throws InterruptedException {
        if (authLatch != null) {
            return authLatch.await(timeout, unit);
        }
        return authenticated;
    }
    
    /**
     * 发送音频数据
     */
    public void sendAudioData(byte[] audioData) {
        if (!authenticated) {
            errorHandler.accept("WebSocket not authenticated");
            return;
        }
        
        try {
            send(audioData);
        } catch (Exception e) {
            log.error("Failed to send audio data", e);
            errorHandler.accept("Failed to send audio data: " + e.getMessage());
        }
    }
    
    /**
     * 发送结束信号
     */
    public void sendEndSignal() {
        if (!authenticated) {
            errorHandler.accept("WebSocket not authenticated");
            return;
        }
        
        try {
            // 发送空数据表示结束
            send(new byte[0]);
        } catch (Exception e) {
            log.error("Failed to send end signal", e);
            errorHandler.accept("Failed to send end signal: " + e.getMessage());
        }
    }
    
    /**
     * 检查连接状态
     */
    public boolean isConnected() {
        return authenticated && !isClosed() && !isClosing();
    }
}