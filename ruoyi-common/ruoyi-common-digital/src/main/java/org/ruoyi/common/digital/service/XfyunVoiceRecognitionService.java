package org.ruoyi.common.digital.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 科大讯飞语音识别服务
 * 基于WebSocket实现实时语音识别
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
@Service
public class XfyunVoiceRecognitionService {

    private static final String HOST_URL = "https://iat.xf-yun.com/v1";
    
    @Value("${xfyun.app-id}")
    private String appId;
    
    @Value("${xfyun.api-key}")
    private String apiKey;
    
    @Value("${xfyun.api-secret}")
    private String apiSecret;
    
    // 音频状态常量
    private static final int STATUS_FIRST_FRAME = 0;
    private static final int STATUS_CONTINUE_FRAME = 1;
    private static final int STATUS_LAST_FRAME = 2;
    
    private static final Gson gson = new Gson();

    /**
     * 语音识别主方法
     */
    public String recognizeAudio(byte[] audioData) {
        if (audioData == null || audioData.length == 0) {
            throw new ServiceException("音频数据不能为空");
        }
        
        try {
            // 构建鉴权URL
            String authUrl = getAuthUrl(HOST_URL, apiKey, apiSecret);
            
            // 创建WebSocket连接
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
                    
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = new Request.Builder().url(url).build();
            
            // 使用CompletableFuture来处理异步结果
            CompletableFuture<String> future = new CompletableFuture<>();
            
            VoiceRecognitionListener listener = new VoiceRecognitionListener(audioData, future);
            WebSocket webSocket = client.newWebSocket(request, listener);
            
            // 等待识别结果，最多等待60秒
            try {
                return future.get(60, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("等待语音识别结果超时", e);
                webSocket.close(1000, "timeout");
                throw new ServiceException("语音识别超时");
            }
            
        } catch (Exception e) {
            log.error("语音识别失败", e);
            throw new ServiceException("语音识别失败: " + e.getMessage());
        }
    }

    /**
     * 构建鉴权URL
     */
    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n")
                .append("date: ").append(date).append("\n")
                .append("GET ").append(url.getPath()).append(" HTTP/1.1");
                
        Charset charset = StandardCharsets.UTF_8;
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", 
                apiKey, "hmac-sha256", "host date request-line", sha);
                
        HttpUrl httpUrl = HttpUrl.parse("https://" + url.getHost() + url.getPath()).newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(charset)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();
                
        return httpUrl.toString();
    }

    /**
     * WebSocket监听器类
     */
    private class VoiceRecognitionListener extends WebSocketListener {
        private final byte[] audioData;
        private final CompletableFuture<String> future;
        private final StringBuilder resultBuilder = new StringBuilder();
        
        public VoiceRecognitionListener(byte[] audioData, CompletableFuture<String> future) {
            this.audioData = audioData;
            this.future = future;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            log.info("WebSocket连接已建立，开始发送音频数据");
            
            // 在新线程中发送音频数据
            new Thread(() -> {
                try {
                    sendAudioData(webSocket);
                } catch (Exception e) {
                    log.error("发送音频数据失败", e);
                    future.completeExceptionally(e);
                }
            }).start();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            try {
                JsonParseResult jsonParse = gson.fromJson(text, JsonParseResult.class);
                if (jsonParse != null) {
                    if (jsonParse.header.code != 0) {
                        String errorMsg = "科大讯飞语音识别错误，code=" + jsonParse.header.code + 
                                ", message=" + jsonParse.header.message;
                        log.error(errorMsg);
                        future.completeExceptionally(new ServiceException(errorMsg));
                        return;
                    }
                    
                    if (jsonParse.payload != null && jsonParse.payload.result != null) {
                        if (jsonParse.payload.result.text != null) {
                            // 处理中间识别结果
                            String decodeRes = decodeResult(jsonParse.payload.result.text);
                            processIntermediateResult(decodeRes);
                        }
                        
                        if (jsonParse.payload.result.status == 2) {
                            // 识别结束
                            log.info("语音识别完成，结果: {}", resultBuilder.toString());
                            future.complete(resultBuilder.toString().trim());
                            webSocket.close(1000, "completed");
                        }
                    }
                }
            } catch (Exception e) {
                log.error("处理语音识别结果失败", e);
                future.completeExceptionally(e);
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            try {
                if (response != null) {
                    int code = response.code();
                    String body = response.body() != null ? response.body().string() : "";
                    log.error("WebSocket连接失败，code: {}, body: {}", code, body);
                    future.completeExceptionally(new ServiceException("WebSocket连接失败: " + body));
                } else {
                    log.error("WebSocket连接失败", t);
                    future.completeExceptionally(new ServiceException("WebSocket连接失败: " + t.getMessage()));
                }
            } catch (IOException e) {
                log.error("处理WebSocket失败回调异常", e);
                future.completeExceptionally(e);
            }
        }

        private void sendAudioData(WebSocket webSocket) throws InterruptedException {
            int frameSize = 1280; // 每一帧音频的大小
            int interval = 40; // 发送间隔
            int status = STATUS_FIRST_FRAME;
            int seq = 0;
            
            int offset = 0;
            while (offset < audioData.length) {
                seq++;
                int len = Math.min(frameSize, audioData.length - offset);
                byte[] frame = Arrays.copyOfRange(audioData, offset, offset + len);
                offset += len;
                
                if (offset >= audioData.length) {
                    status = STATUS_LAST_FRAME;
                }
                
                String json = buildAudioFrame(status, seq, frame);
                webSocket.send(json);
                
                if (status == STATUS_FIRST_FRAME) {
                    log.info("第一帧音频发送完毕");
                    status = STATUS_CONTINUE_FRAME;
                } else if (status == STATUS_LAST_FRAME) {
                    log.info("最后一帧音频发送完毕");
                    break;
                }
                
                Thread.sleep(interval);
            }
        }

        private String buildAudioFrame(int status, int seq, byte[] audioData) {
            JsonObject json = new JsonObject();
            
            // header
            JsonObject header = new JsonObject();
            header.addProperty("app_id", appId);
            header.addProperty("status", status);
            json.add("header", header);
            
            // parameter (只在第一帧发送)
            if (status == STATUS_FIRST_FRAME) {
                JsonObject parameter = new JsonObject();
                JsonObject iat = new JsonObject();
                iat.addProperty("domain", "slm");
                iat.addProperty("language", "zh_cn");
                iat.addProperty("accent", "mandarin");
                iat.addProperty("eos", 6000);
                iat.addProperty("vinfo", 1);
                iat.addProperty("dwa", "wpgs");
                
                JsonObject result = new JsonObject();
                result.addProperty("encoding", "utf8");
                result.addProperty("compress", "raw");
                result.addProperty("format", "json");
                iat.add("result", result);
                
                parameter.add("iat", iat);
                json.add("parameter", parameter);
            }
            
            // payload
            JsonObject payload = new JsonObject();
            JsonObject audio = new JsonObject();
            audio.addProperty("encoding", "raw");
            audio.addProperty("sample_rate", 16000);
            audio.addProperty("channels", 1);
            audio.addProperty("bit_depth", 16);
            audio.addProperty("seq", seq);
            audio.addProperty("status", status == STATUS_FIRST_FRAME ? 0 : (status == STATUS_CONTINUE_FRAME ? 1 : 2));
            
            if (status == STATUS_LAST_FRAME) {
                audio.addProperty("audio", "");
            } else {
                audio.addProperty("audio", Base64.getEncoder().encodeToString(audioData));
            }
            
            payload.add("audio", audio);
            json.add("payload", payload);
            
            return json.toString();
        }

        private String decodeResult(String encodedText) {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        }

        private void processIntermediateResult(String result) {
            try {
                JsonParseText jsonParseText = gson.fromJson(result, JsonParseText.class);
                if (jsonParseText.ws != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Ws ws : jsonParseText.ws) {
                        if (ws.cw != null) {
                            for (Cw cw : ws.cw) {
                                sb.append(cw.w);
                            }
                        }
                    }
                    
                    String currentResult = sb.toString();
                    if (!currentResult.trim().isEmpty()) {
                        // 根据pgs字段决定如何处理结果
                        if ("apd".equals(jsonParseText.pgs)) {
                            // 追加到现有结果
                            resultBuilder.append(currentResult);
                        } else if ("rpl".equals(jsonParseText.pgs)) {
                            // 替换部分结果（简化处理，直接追加）
                            resultBuilder.append(currentResult);
                        } else {
                            // 默认追加
                            resultBuilder.append(currentResult);
                        }
                        
                        log.debug("中间识别结果: {}", currentResult);
                    }
                }
            } catch (Exception e) {
                log.warn("解析中间结果失败", e);
            }
        }
    }

    // 结果解析类
    private static class JsonParseResult {
        public Header header;
        public Payload payload;
    }

    private static class Header {
        public int code;
        public String message;
        @SuppressWarnings("unused")
        public String sid;
        @SuppressWarnings("unused")
        public int status;
    }

    private static class Payload {
        public Result result;
    }

    private static class Result {
        public String text;
        public int status;
    }

    private static class JsonParseText {
        public List<Ws> ws;
        public String pgs;
        @SuppressWarnings("unused")
        public List<Integer> rg;
    }

    private static class Ws {
        public List<Cw> cw;
    }

    private static class Cw {
        public String w;
    }
}
