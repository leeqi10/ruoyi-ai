package org.ruoyi.common.digital.xfyun.entity;

import lombok.Data;

/**
 * 科大讯飞实时语音转写请求参数
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunTranscriptionRequest {
    
    /**
     * 应用ID
     */
    private String appId;
    
    /**
     * API密钥
     */
    private String apiKey;
    
    /**
     * 音频数据
     */
    private byte[] audioData;
    
    /**
     * 音频格式
     */
    private String audioFormat = "raw";
    
    /**
     * 采样率
     */
    private String sampleRate = "16000";
    
    /**
     * 数据编码
     */
    private String dataType = "raw";
    
    /**
     * 语言
     */
    private String language = "zh_cn";
    
    /**
     * 领域
     */
    private String domain = "iat";
    
    /**
     * 是否开启标点符号
     */
    private String punctuation = "1";
    
    /**
     * 是否开启数字转换
     */
    private String numberConvert = "zh";
    
    /**
     * 是否实时返回结果
     */
    private Boolean realTime = true;
    
    /**
     * 结果回调URL（可选）
     */
    private String callbackUrl;
}
