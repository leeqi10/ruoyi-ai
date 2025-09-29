package org.ruoyi.digital.human.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.digital.human.domain.DigitalHumanSession;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数字人会话视图对象 digital_human_session
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DigitalHumanSession.class)
public class DigitalHumanSessionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 会话ID
     */
    @ExcelProperty(value = "会话ID")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 数字人ID
     */
    @ExcelProperty(value = "数字人ID")
    private Long digitalHumanId;

    /**
     * 数字人名称
     */
    private String digitalHumanName;

    /**
     * 数字人头像
     */
    private String digitalHumanAvatar;

    /**
     * 会话类型(1-文字聊天 2-语音通话)
     */
    @ExcelProperty(value = "会话类型")
    private String sessionType;

    /**
     * 会话标题
     */
    @ExcelProperty(value = "会话标题")
    private String title;

    /**
     * 最后一条消息
     */
    @ExcelProperty(value = "最后一条消息")
    private String lastMessage;

    /**
     * 消息数量
     */
    @ExcelProperty(value = "消息数量")
    private Integer messageCount;

    /**
     * 通话时长(秒)
     */
    @ExcelProperty(value = "通话时长")
    private Integer duration;

    /**
     * 状态(0-结束 1-进行中)
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;
}
