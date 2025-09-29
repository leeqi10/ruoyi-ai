package org.ruoyi.common.digital.xfyun.entity;

import lombok.Data;

/**
 * 科大讯飞文字转语音响应
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunTtsResponse {

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 错误码，0表示成功
     */
    private String code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 音频数据（Base64编码）
     */
    private String audioData;

    /**
     * 音频文件URL（如果保存到文件系统）
     */
    private String audioUrl;

    /**
     * 音频时长（秒）
     */
    private Integer duration;

    /**
     * 音频大小（字节）
     */
    private Long audioSize;

    /**
     * 音频格式
     */
    private String audioFormat;

    /**
     * 任务ID（异步调用时使用）
     */
    private String taskId;

}
