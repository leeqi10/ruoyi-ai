package org.ruoyi.digital.human.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 数字人会话对象 digital_human_session
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("digital_human_session")
public class DigitalHumanSession extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 数字人ID
     */
    private Long digitalHumanId;

    /**
     * 会话类型(1-文字聊天 2-语音通话)
     */
    private String sessionType;

    /**
     * 会话标题
     */
    private String title;

    /**
     * 最后一条消息
     */
    private String lastMessage;

    /**
     * 消息数量
     */
    private Integer messageCount;

    /**
     * 通话时长(秒)
     */
    private Integer duration;

    /**
     * 状态(0-结束 1-进行中)
     */
    private String status;

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
