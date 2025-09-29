package org.ruoyi.common.digital.xfyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 科大讯飞API错误响应
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunErrorResponse {
    
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
     * 错误详情
     */
    @JsonProperty("desc")
    private String description;
    
    /**
     * 请求唯一标识
     */
    @JsonProperty("sid")
    private String sid;
}
