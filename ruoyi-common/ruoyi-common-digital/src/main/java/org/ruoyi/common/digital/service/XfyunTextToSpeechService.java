package org.ruoyi.common.digital.service;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import java.util.Objects;
import org.ruoyi.common.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.enums.ReadyState;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 科大讯飞文字转语音服务
 * 基于WebSocket实现文字转语音功能
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
@Service
public class XfyunTextToSpeechService {

    private static final String HOST_URL = "https://tts-api.xfyun.cn/v2/tts";
    
    @Value("${xfyun.app-id}")
    private String appId;
    
    @Value("${xfyun.api-key}")
    private String apiKey;
    
    @Value("${xfyun.api-secret}")
    private String apiSecret;
    
    @Value("${ruoyi.profile:/tmp/ruoyi-uploads}")
    private String uploadPath;

    private static final Gson gson = new Gson();

    // TTS配置常量
    private static final String TTE = "UTF8"; // 文本编码格式
    private static final String VCN = "x4_yezi"; // 发音人参数
    private static final String AUE = "raw"; // 音频编码格式
    private static final int PITCH = 50; // 语调
    private static final int SPEED = 50; // 语速

    /**
     * 文字转语音主方法
     */
    public String convertTextToSpeech(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new ServiceException("转换文本不能为空");
        }

        try {
            // 构建鉴权URL
            String authUrl = getAuthUrl(HOST_URL, apiKey, apiSecret);
            String wsUrl = authUrl.replace("https://", "wss://");
            
            // 生成唯一的输出文件路径
            String fileName = "tts_" + System.currentTimeMillis() + ".pcm";
            String outputFilePath = uploadPath + File.separator + "audio" + File.separator + fileName;
            
            // 确保目录存在
            File outputDir = new File(outputFilePath).getParentFile();
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            // 创建输出流
            FileOutputStream outputStream = new FileOutputStream(outputFilePath);
            
            // 执行WebSocket TTS转换
            CompletableFuture<String> future = new CompletableFuture<>();
            executeTextToSpeech(wsUrl, text, outputStream, outputFilePath, future);
            
            // 等待转换完成，最多等待30秒
            future.get(30, TimeUnit.SECONDS);
            
            // 返回相对URL路径
            return "/audio/" + fileName;
            
        } catch (Exception e) {
            log.error("文字转语音失败", e);
            throw new ServiceException("文字转语音失败: " + e.getMessage());
        }
    }

    /**
     * 执行WebSocket文字转语音
     */
    private void executeTextToSpeech(String wsUrl, String text, FileOutputStream outputStream, 
                                   String outputFilePath, CompletableFuture<String> future) {
        try {
            URI uri = new URI(wsUrl);
            WebSocketClient webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("TTS WebSocket连接已建立");
                }

                @Override
                public void onMessage(String message) {
                    try {
                        JsonParseResult result = gson.fromJson(message, JsonParseResult.class);
                        if (result.code != 0) {
                            String errorMsg = "TTS转换错误，错误码：" + result.code + ", sid：" + result.sid;
                            log.error(errorMsg);
                            future.completeExceptionally(new ServiceException(errorMsg));
                            return;
                        }

                        if (result.data != null) {
                            // 写入音频数据
                            byte[] audioData = Base64.getDecoder().decode(result.data.audio);
                            outputStream.write(audioData);
                            outputStream.flush();

                            // 检查是否完成
                            if (result.data.status == 2) {
                                try {
                                    outputStream.close();
                                    log.info("TTS转换完成，文件保存路径：{}", outputFilePath);
                                    future.complete(outputFilePath);
                                } catch (IOException e) {
                                    log.error("关闭输出流失败", e);
                                    future.completeExceptionally(e);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("处理TTS响应失败", e);
                        future.completeExceptionally(e);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("TTS WebSocket连接已关闭，code：{}, reason：{}", code, reason);
                }

                @Override
                public void onError(Exception e) {
                    log.error("TTS WebSocket连接错误", e);
                    future.completeExceptionally(e);
                }
            };

            // 建立连接
            webSocketClient.connect();
            
            // 等待连接建立
            long startTime = System.currentTimeMillis();
            while (!webSocketClient.getReadyState().equals(ReadyState.OPEN)) {
                Thread.sleep(100);
                if (System.currentTimeMillis() - startTime > 10000) { // 10秒超时
                    throw new ServiceException("WebSocket连接超时");
                }
            }

            // 发送TTS请求
            sendTtsRequest(webSocketClient, text);

        } catch (Exception e) {
            log.error("执行TTS转换失败", e);
            future.completeExceptionally(e);
        }
    }

    /**
     * 发送TTS请求
     */
    private void sendTtsRequest(WebSocketClient webSocketClient, String text) {
        try {
            String requestJson = buildTtsRequest(text);
            webSocketClient.send(requestJson);
            log.debug("TTS请求已发送：{}", requestJson);
        } catch (Exception e) {
            log.error("发送TTS请求失败", e);
            throw new ServiceException("发送TTS请求失败: " + e.getMessage());
        }
    }

    /**
     * 构建TTS请求JSON
     */
    private String buildTtsRequest(String text) {
        Map<String, Object> request = new HashMap<>();
        
        // common部分
        Map<String, Object> common = new HashMap<>();
        common.put("app_id", appId);
        request.put("common", common);
        
        // business部分
        Map<String, Object> business = new HashMap<>();
        business.put("aue", AUE);
        business.put("tte", TTE);
        business.put("ent", "intp65");
        business.put("vcn", VCN);
        business.put("pitch", PITCH);
        business.put("speed", SPEED);
        request.put("business", business);
        
        // data部分
        Map<String, Object> data = new HashMap<>();
        data.put("status", 2);
        data.put("text", Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8)));
        request.put("data", data);
        
        return gson.toJson(request);
    }

    /**
     * 构建鉴权URL
     */
    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
                
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", 
                apiKey, "hmac-sha256", "host date request-line", sha);
                
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath()))
                .newBuilder()
                .addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)))
                .addQueryParameter("date", date)
                .addQueryParameter("host", url.getHost())
                .build();

        return httpUrl.toString();
    }

    /**
     * 批量文字转语音（用于长文本）
     */
    public List<String> convertTextToSpeechBatch(List<String> textList) {
        List<String> audioUrls = new ArrayList<>();
        for (String text : textList) {
            try {
                String audioUrl = convertTextToSpeech(text);
                audioUrls.add(audioUrl);
            } catch (Exception e) {
                log.error("批量TTS转换失败，文本：{}", text, e);
                audioUrls.add(null); // 添加null表示该文本转换失败
            }
        }
        return audioUrls;
    }

    /**
     * 验证TTS服务是否可用
     */
    public boolean isServiceAvailable() {
        try {
            String audioUrl = convertTextToSpeech("测试");
            log.info("TTS服务可用，测试音频URL: {}", audioUrl);
            return true;
        } catch (Exception e) {
            log.warn("TTS服务不可用", e);
            return false;
        }
    }

    /**
     * 清理过期的音频文件
     * 删除超过指定时间的音频文件以节省存储空间
     */
    public void cleanupExpiredAudioFiles(int maxAgeInHours) {
        try {
            File audioDir = new File(uploadPath + File.separator + "audio");
            if (!audioDir.exists()) {
                return;
            }

            long currentTime = System.currentTimeMillis();
            long maxAge = maxAgeInHours * 60 * 60 * 1000L; // 转换为毫秒

            File[] files = audioDir.listFiles((dir, name) -> name.startsWith("tts_") && name.endsWith(".pcm"));
            if (files != null) {
                for (File file : files) {
                    if (currentTime - file.lastModified() > maxAge) {
                        boolean deleted = file.delete();
                        log.info("清理过期音频文件: {}, 删除结果: {}", file.getName(), deleted);
                    }
                }
            }
        } catch (Exception e) {
            log.error("清理过期音频文件失败", e);
        }
    }

    /**
     * 获取音频文件信息
     */
    public Map<String, Object> getAudioFileInfo(String audioUrl) {
        Map<String, Object> info = new HashMap<>();
        try {
            if (audioUrl != null && audioUrl.startsWith("/audio/")) {
                String fileName = audioUrl.substring("/audio/".length());
                File audioFile = new File(uploadPath + File.separator + "audio" + File.separator + fileName);
                
                info.put("exists", audioFile.exists());
                if (audioFile.exists()) {
                    info.put("size", audioFile.length());
                    info.put("lastModified", new Date(audioFile.lastModified()));
                    info.put("path", audioFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            log.error("获取音频文件信息失败", e);
            info.put("error", e.getMessage());
        }
        return info;
    }

    // JSON解析类
    private static class JsonParseResult {
        public int code;
        public String sid;
        public Data data;
    }

    private static class Data {
        public int status;
        public String audio;
    }
}
