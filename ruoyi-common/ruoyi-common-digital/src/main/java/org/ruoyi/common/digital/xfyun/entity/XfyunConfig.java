package org.ruoyi.common.digital.xfyun.entity;

import lombok.Data;
import org.ruoyi.common.digital.xfyun.constant.XfyunConst;

/**
 * 科大讯飞API配置
 * 使用常量定义默认值
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunConfig {
    
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
    private String wsUrl = XfyunConst.DEFAULT_WS_URL;
    
    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout = XfyunConst.DEFAULT_CONNECT_TIMEOUT;
    
    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout = XfyunConst.DEFAULT_READ_TIMEOUT;
    
    /**
     * 写入超时时间（毫秒）
     */
    private Integer writeTimeout = XfyunConst.DEFAULT_WRITE_TIMEOUT;
    
    /**
     * 重试次数
     */
    private Integer retryCount = XfyunConst.DEFAULT_RETRY_COUNT;
    
    /**
     * 重试间隔（毫秒）
     */
    private Integer retryInterval = XfyunConst.DEFAULT_RETRY_INTERVAL;
    
    /**
     * 音频格式
     */
    private String audioFormat = XfyunConst.AudioFormat.RAW;
    
    /**
     * 采样率
     */
    private String sampleRate = XfyunConst.SampleRate.RATE_16000;
    
    /**
     * 数据编码
     */
    private String dataType = XfyunConst.DataType.RAW;
    
    /**
     * 语言
     */
    private String language = XfyunConst.Language.ZH_CN;
    
    /**
     * 领域
     */
    private String domain = XfyunConst.Domain.IAT;
    
    /**
     * 是否开启标点符号
     */
    private String punctuation = XfyunConst.Punctuation.ENABLED;
    
    /**
     * 是否开启数字转换
     */
    private String numberConvert = XfyunConst.NumberConvert.CHINESE;
    
    /**
     * 音频分片大小（字节）
     */
    private Integer chunkSize = XfyunConst.DEFAULT_CHUNK_SIZE;
    
    /**
     * 分片发送间隔（毫秒）
     */
    private Integer chunkInterval = XfyunConst.DEFAULT_CHUNK_INTERVAL;
    
    /**
     * 最大连接池大小
     */
    private Integer maxConnections = 10;
    
    /**
     * 连接保活时间（毫秒）
     */
    private Integer keepAliveDuration = 300000;
    
    /**
     * 是否启用调试日志
     */
    private Boolean debugEnabled = false;
    
    /**
     * 是否启用WebSocket通信日志
     */
    private Boolean websocketLogEnabled = false;
}
