package org.ruoyi.digital.human.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.digital.human.domain.DigitalHumanMessage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数字人消息视图对象 digital_human_message
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DigitalHumanMessage.class)
public class DigitalHumanMessageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @ExcelProperty(value = "消息ID")
    private Long id;

    /**
     * 会话ID
     */
    @ExcelProperty(value = "会话ID")
    private Long sessionId;

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
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    private String content;

    /**
     * 消息类型(1-文本 2-语音 3-图片)
     */
    @ExcelProperty(value = "消息类型")
    private String messageType;

    /**
     * 角色(user-用户 assistant-助手)
     */
    @ExcelProperty(value = "角色")
    private String role;

    /**
     * 语音文件地址
     */
    @ExcelProperty(value = "语音文件地址")
    private String audioUrl;

    /**
     * 语音时长(秒)
     */
    @ExcelProperty(value = "语音时长")
    private Integer audioDuration;

    /**
     * Token数量
     */
    @ExcelProperty(value = "Token数量")
    private Integer tokens;

    /**
     * 使用的模型名称
     */
    @ExcelProperty(value = "使用的模型名称")
    private String modelName;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
