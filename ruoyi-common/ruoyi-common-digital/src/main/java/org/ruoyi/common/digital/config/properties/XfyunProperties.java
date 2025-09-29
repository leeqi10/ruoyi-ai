package org.ruoyi.common.digital.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 科大讯飞WebSocket配置属性
 * 仿照OpenAI的配置方式
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
@ConfigurationProperties(prefix = "xfyun")
public class XfyunProperties {

    /**
     * 是否启用科大讯飞服务
     */
    private Boolean enabled = false;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * WebSocket连接URL
     */
    private String wsUrl = "wss://rtasr.xfyun.cn/v1/ws";

    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout = 30000;

    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout = 60000;

    /**
     * 写入超时时间（毫秒）
     */
    private Integer writeTimeout = 60000;

    /**
     * 重试次数
     */
    private Integer retryCount = 3;

    /**
     * 重试间隔（毫秒）
     */
    private Integer retryInterval = 1000;

    /**
     * 最大连接池大小
     */
    private Integer maxConnections = 10;

    /**
     * 连接保活时间（毫秒）
     */
    private Integer keepAliveDuration = 300000;

    /**
     * 音频配置
     */
    private Audio audio = new Audio();

    /**
     * 语言配置
     */
    private Language language = new Language();

    /**
     * 日志配置
     */
    private Log log = new Log();

    @Data
    public static class Audio {
        /**
         * 默认音频格式
         */
        private String format = "raw";

        /**
         * 默认采样率
         */
        private String sampleRate = "16000";

        /**
         * 默认数据编码
         */
        private String dataType = "raw";

        /**
         * 默认声道数
         */
        private Integer channels = 1;

        /**
         * 默认位深度
         */
        private Integer bitDepth = 16;

        /**
         * 音频分片大小（字节）
         */
        private Integer chunkSize = 1280;

        /**
         * 分片发送间隔（毫秒）
         */
        private Integer chunkInterval = 40;
    }

    @Data
    public static class Language {
        /**
         * 默认语言
         */
        private String defaultLanguage = "zh_cn";

        /**
         * 默认领域
         */
        private String defaultDomain = "iat";

        /**
         * 是否开启标点符号
         */
        private String punctuation = "1";

        /**
         * 是否开启数字转换
         */
        private String numberConvert = "zh";

        /**
         * 是否开启热词
         */
        private Boolean enableHotword = false;

        /**
         * 热词ID列表
         */
        private String hotwordIds;
    }

    @Data
    public static class Log {
        /**
         * 是否启用调试日志
         */
        private Boolean debug = false;

        /**
         * 是否启用WebSocket通信日志
         */
        private Boolean websocket = false;

        /**
         * 是否启用音频处理日志
         */
        private Boolean audio = false;

        /**
         * 是否启用性能监控日志
         */
        private Boolean performance = false;
    }
}
