package org.ruoyi.common.digital.xfyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 科大讯飞实时语音转写API认证响应
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunAuthResponse {
    
    /**
     * 错误码
     */
    @JsonProperty("code")
    private Integer code;
    
    /**
     * 错误信息
     */
    @JsonProperty("message")
    private String message;
    
    /**
     * 数据
     */
    @JsonProperty("data")
    private AuthData data;
    
    /**
     * 请求唯一标识
     */
    @JsonProperty("sid")
    private String sid;
    
    @Data
    public static class AuthData {
        /**
         * 认证状态
         */
        @JsonProperty("status")
        private Integer status;
        
        /**
         * 状态描述
         */
        @JsonProperty("desc")
        private String description;
    }
}
