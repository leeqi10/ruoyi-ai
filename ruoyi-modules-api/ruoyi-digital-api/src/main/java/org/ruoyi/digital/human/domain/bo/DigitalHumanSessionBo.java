package org.ruoyi.digital.human.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.digital.human.domain.DigitalHumanSession;

/**
 * 数字人会话业务对象 digital_human_session
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DigitalHumanSession.class, reverseConvertGenerate = false)
public class DigitalHumanSessionBo extends BaseEntity {

    /**
     * 会话ID
     */
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
}
