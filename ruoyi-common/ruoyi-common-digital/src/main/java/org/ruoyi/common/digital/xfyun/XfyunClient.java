package org.ruoyi.common.digital.xfyun;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.ruoyi.common.digital.xfyun.websocket.XfyunWebSocketClient;

import java.util.function.Consumer;

/**
 * 科大讯飞客户端
 * 仿照OpenAI的Builder模式设计
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
@Getter
public class XfyunClient {
    
    /**
     * 应用ID
     */
    @Getter
    private final String appId;
    
    /**
     * API密钥
     */
    @Getter
    private final String apiKey;
    
    /**
     * WebSocket连接URL
     */
    @Getter
    private final String wsUrl;
    
    /**
     * 连接超时时间（毫秒）
     */
    @Getter
    private final Integer connectTimeout;
    
    /**
     * 读取超时时间（毫秒）
     */
    @Getter
    private final Integer readTimeout;
    
    /**
     * 重试次数
     */
    @Getter
    private final Integer retryCount;
    
    /**
     * 音频格式
     */
    @Getter
    private final String audioFormat;
    
    /**
     * 采样率
     */
    @Getter
    private final String sampleRate;
    
    /**
     * 数据编码
     */
    @Getter
    private final String dataType;
    
    /**
     * 语言
     */
    @Getter
    private final String language;
    
    /**
     * 领域
     */
    @Getter
    private final String domain;
    
    /**
     * 是否开启标点符号
     */
    @Getter
    private final String punctuation;
    
    /**
     * 是否开启数字转换
     */
    @Getter
    private final String numberConvert;
    
    /**
     * API实例
     */
    @Getter
    private final XfyunApi xfyunApi;
    
    /**
     * 构造器
     *
     * @return XfyunClient.Builder
     */
    public static Builder builder() {
        return new Builder();
    }
    
    /**
     * 构造方法
     *
     * @param builder 构建器
     */
    private XfyunClient(Builder builder) {
        if (builder.appId == null || builder.appId.trim().isEmpty()) {
            throw new IllegalArgumentException("AppId不能为空");
        }
        this.appId = builder.appId;
        
        if (builder.apiKey == null || builder.apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("ApiKey不能为空");
        }
        this.apiKey = builder.apiKey;
        
        // 设置默认值
        this.wsUrl = builder.wsUrl != null ? builder.wsUrl : "wss://rtasr.xfyun.cn/v1/ws";
        this.connectTimeout = builder.connectTimeout != null ? builder.connectTimeout : 30000;
        this.readTimeout = builder.readTimeout != null ? builder.readTimeout : 60000;
        this.retryCount = builder.retryCount != null ? builder.retryCount : 3;
        this.audioFormat = builder.audioFormat != null ? builder.audioFormat : "raw";
        this.sampleRate = builder.sampleRate != null ? builder.sampleRate : "16000";
        this.dataType = builder.dataType != null ? builder.dataType : "raw";
        this.language = builder.language != null ? builder.language : "zh_cn";
        this.domain = builder.domain != null ? builder.domain : "iat";
        this.punctuation = builder.punctuation != null ? builder.punctuation : "1";
        this.numberConvert = builder.numberConvert != null ? builder.numberConvert : "zh";
        
        // 创建API实例
        XfyunConfig config = buildConfig();
        this.xfyunApi = XfyunApiFactory.create(config);
        
        log.info("科大讯飞客户端初始化完成，AppId: {}, WsUrl: {}", this.appId, this.wsUrl);
    }
    
    /**
     * 构建配置对象
     *
     * @return XfyunConfig配置对象
     */
    private XfyunConfig buildConfig() {
        XfyunConfig config = new XfyunConfig();
        config.setAppId(this.appId);
        config.setApiKey(this.apiKey);
        config.setWsUrl(this.wsUrl);
        config.setConnectTimeout(this.connectTimeout);
        config.setReadTimeout(this.readTimeout);
        config.setRetryCount(this.retryCount);
        config.setAudioFormat(this.audioFormat);
        config.setSampleRate(this.sampleRate);
        config.setDataType(this.dataType);
        config.setLanguage(this.language);
        config.setDomain(this.domain);
        config.setPunctuation(this.punctuation);
        config.setNumberConvert(this.numberConvert);
        return config;
    }
    
    /**
     * 创建WebSocket客户端
     *
     * @param responseHandler 响应处理器
     * @param errorHandler 错误处理器
     * @return WebSocket客户端
     */
    public XfyunWebSocketClient createWebSocketClient(
            Consumer<org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionResponse> responseHandler,
            Consumer<String> errorHandler) {
        XfyunConfig config = buildConfig();
        return new XfyunWebSocketClient(config, responseHandler, errorHandler);
    }
    
    /**
     * 构建器类
     */
    public static final class Builder {
        private String appId;
        private String apiKey;
        private String wsUrl;
        private Integer connectTimeout;
        private Integer readTimeout;
        private Integer retryCount;
        private String audioFormat;
        private String sampleRate;
        private String dataType;
        private String language;
        private String domain;
        private String punctuation;
        private String numberConvert;
        
        public Builder() {
        }
        
        /**
         * 设置应用ID
         *
         * @param appId 应用ID
         * @return Builder对象
         */
        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }
        
        /**
         * 设置API密钥
         *
         * @param apiKey API密钥
         * @return Builder对象
         */
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        
        /**
         * 设置WebSocket连接URL
         *
         * @param wsUrl WebSocket连接URL
         * @return Builder对象
         */
        public Builder wsUrl(String wsUrl) {
            this.wsUrl = wsUrl;
            return this;
        }
        
        /**
         * 设置连接超时时间
         *
         * @param connectTimeout 连接超时时间（毫秒）
         * @return Builder对象
         */
        public Builder connectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }
        
        /**
         * 设置读取超时时间
         *
         * @param readTimeout 读取超时时间（毫秒）
         * @return Builder对象
         */
        public Builder readTimeout(Integer readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }
        
        /**
         * 设置重试次数
         *
         * @param retryCount 重试次数
         * @return Builder对象
         */
        public Builder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }
        
        /**
         * 设置音频格式
         *
         * @param audioFormat 音频格式
         * @return Builder对象
         */
        public Builder audioFormat(String audioFormat) {
            this.audioFormat = audioFormat;
            return this;
        }
        
        /**
         * 设置采样率
         *
         * @param sampleRate 采样率
         * @return Builder对象
         */
        public Builder sampleRate(String sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }
        
        /**
         * 设置数据编码
         *
         * @param dataType 数据编码
         * @return Builder对象
         */
        public Builder dataType(String dataType) {
            this.dataType = dataType;
            return this;
        }
        
        /**
         * 设置语言
         *
         * @param language 语言
         * @return Builder对象
         */
        public Builder language(String language) {
            this.language = language;
            return this;
        }
        
        /**
         * 设置领域
         *
         * @param domain 领域
         * @return Builder对象
         */
        public Builder domain(String domain) {
            this.domain = domain;
            return this;
        }
        
        /**
         * 设置标点符号
         *
         * @param punctuation 标点符号
         * @return Builder对象
         */
        public Builder punctuation(String punctuation) {
            this.punctuation = punctuation;
            return this;
        }
        
        /**
         * 设置数字转换
         *
         * @param numberConvert 数字转换
         * @return Builder对象
         */
        public Builder numberConvert(String numberConvert) {
            this.numberConvert = numberConvert;
            return this;
        }
        
        /**
         * 构建客户端实例
         *
         * @return XfyunClient实例
         */
        public XfyunClient build() {
            return new XfyunClient(this);
        }
    }
}
