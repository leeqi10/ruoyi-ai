package org.ruoyi.common.digital.xfyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 科大讯飞实时语音转写API认证请求参数
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
@Builder
public class XfyunAuthRequest {
    
    /**
     * 应用ID
     */
    @JsonProperty("appid")
    private String appId;
    
    /**
     * 时间戳
     */
    @JsonProperty("ts")
    private String timestamp;
    
    /**
     * 签名
     */
    @JsonProperty("signa")
    private String signature;
    
    /**
     * 数据格式，可选值：raw, speex, opus, speex-wb, opus-wb
     */
    @JsonProperty("aue")
    @Builder.Default
    private String audioFormat = "raw";
    
    /**
     * 采样率，可选值：16000, 8000
     */
    @JsonProperty("sample_rate")
    @Builder.Default
    private String sampleRate = "16000";
    
    /**
     * 数据编码，可选值：lame, speex, opus
     */
    @JsonProperty("data_type")
    @Builder.Default
    private String dataType = "raw";
    
    /**
     * 语言，可选值：zh_cn, en_us
     */
    @JsonProperty("language")
    @Builder.Default
    private String language = "zh_cn";
    
    /**
     * 领域，可选值：iat, medical, finance, education, car, smart_home
     */
    @JsonProperty("domain")
    @Builder.Default
    private String domain = "iat";
    
    /**
     * 是否开启标点符号
     */
    @JsonProperty("pd")
    @Builder.Default
    private String punctuation = "1";
    
    /**
     * 是否开启数字转换
     */
    @JsonProperty("rlang")
    @Builder.Default
    private String numberConvert = "zh";
}
