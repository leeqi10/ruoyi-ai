package org.ruoyi.digital.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 数字人语音通话请求类
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
@Schema(description = "数字人语音通话请求")
public class DigitalHumanVoiceChatRequest {

    /**
     * 会话ID（可选，如果不传则创建新会话）
     */
    @Schema(description = "会话ID，如果不传则创建新会话")
    private Long sessionId;

    /**
     * 数字人ID（必填）
     */
    @NotNull(message = "数字人ID不能为空")
    @Schema(description = "数字人ID")
    private Long digitalHumanId;

    /**
     * 消息内容（文本消息时必填）
     */
    @Schema(description = "消息内容")
    private String content;

    /**
     * 消息类型（1-文本 2-语音 ）
     */
    @NotNull(message = "消息类型不能为空")
    @Schema(description = "消息类型：1-文本 2-语音 ", allowableValues = {"1", "2"})
    private String messageType;

    /**
     * 语音文件（语音消息时需要）
     */
    @Schema(description = "语音文件")
    private MultipartFile audioFile;

    /**
     * 使用的AI模型（可选）
     */
    @Schema(description = "AI模型名称")
    private String model;

    /**
     * 输出类型
     */
    @Schema(description = "输出类型：1-文本 2-文本+语音")
    private String outputType = "2"; // 语音通话默认输出语音

}
