package org.ruoyi.digital.human.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 数字人消息对象 digital_human_message
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("digital_human_message")
public class DigitalHumanMessage extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 会话ID
     */
    private Long sessionId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 数字人ID
     */
    private Long digitalHumanId;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型(1-文本 2-语音 3-图片)
     */
    private String messageType;

    /**
     * 角色(user-用户 assistant-助手)
     */
    private String role;

    /**
     * 语音文件地址
     */
    private String audioUrl;

    /**
     * 语音时长(秒)
     */
    private Integer audioDuration;

    /**
     * Token数量
     */
    private Integer tokens;

    /**
     * 使用的模型名称
     */
    private String modelName;

    /**
     * 租户编号
     */
    private String tenantId;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 备注
     */
    private String remark;
}
