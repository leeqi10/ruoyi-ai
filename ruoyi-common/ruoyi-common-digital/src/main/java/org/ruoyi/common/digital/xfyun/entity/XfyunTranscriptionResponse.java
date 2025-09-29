package org.ruoyi.common.digital.xfyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 科大讯飞实时语音转写响应
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunTranscriptionResponse {
    
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
    private TranscriptionData data;
    
    /**
     * 请求唯一标识
     */
    @JsonProperty("sid")
    private String sid;
    
    @Data
    public static class TranscriptionData {
        /**
         * 状态
         */
        @JsonProperty("status")
        private Integer status;
        
        /**
         * 结果
         */
        @JsonProperty("result")
        private TranscriptionResult result;
    }
    
    @Data
    public static class TranscriptionResult {
        /**
         * 转写结果类型：0-中间结果，1-最终结果
         */
        @JsonProperty("type")
        private Integer type;
        
        /**
         * 转写结果
         */
        @JsonProperty("ws")
        private List<WordSegment> wordSegments;
        
        /**
         * 是否结束
         */
        @JsonProperty("is_final")
        private Boolean isFinal;
    }
    
    @Data
    public static class WordSegment {
        /**
         * 词在句子中的位置
         */
        @JsonProperty("bg")
        private Integer begin;
        
        /**
         * 词在句子中的结束位置
         */
        @JsonProperty("ed")
        private Integer end;
        
        /**
         * 词内容
         */
        @JsonProperty("cw")
        private List<WordContent> wordContents;
    }
    
    @Data
    public static class WordContent {
        /**
         * 词
         */
        @JsonProperty("w")
        private String word;
        
        /**
         * 词在句子中的位置
         */
        @JsonProperty("bg")
        private Integer begin;
        
        /**
         * 词在句子中的结束位置
         */
        @JsonProperty("ed")
        private Integer end;
        
        /**
         * 置信度
         */
        @JsonProperty("sc")
        private Double confidence;
    }
}
