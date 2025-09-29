package org.ruoyi.digital.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数字人聊天响应类
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
@Schema(description = "数字人聊天响应")
public class DigitalHumanChatResponse {

    /**
     * 会话ID
     */
    @Schema(description = "会话ID")
    private Long sessionId;

    /**
     * 消息ID
     */
    @Schema(description = "消息ID")
    private Long messageId;

    /**
     * 数字人ID
     */
    @Schema(description = "数字人ID")
    private Long digitalHumanId;

    /**
     * 数字人名称
     */
    @Schema(description = "数字人名称")
    private String digitalHumanName;

    /**
     * AI回复内容
     */
    @Schema(description = "AI回复内容")
    private String content;

    /**
     * 语音文件地址（如果输出包含语音）
     */
    @Schema(description = "语音文件地址")
    private String audioUrl;

    /**
     * 语音时长（秒）
     */
    @Schema(description = "语音时长")
    private Integer audioDuration;

    /**
     * Token使用量
     */
    @Schema(description = "Token使用量")
    private Integer tokens;

    /**
     * 使用的模型
     */
    @Schema(description = "使用的模型")
    private String model;

    /**
     * 处理时间（毫秒）
     */
    @Schema(description = "处理时间")
    private Long processTime;

    /**
     * 是否成功
     */
    @Schema(description = "是否成功")
    private Boolean success = true;

    /**
     * 错误信息
     */
    @Schema(description = "错误信息")
    private String errorMessage;

}
