package org.ruoyi.digital.human.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.digital.human.domain.DigitalHumanVoice;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数字人音色视图对象 digital_human_voice
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DigitalHumanVoice.class)
public class DigitalHumanVoiceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 音色ID
     */
    @ExcelProperty(value = "音色ID")
    private Long id;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 音色名称
     */
    @ExcelProperty(value = "音色名称")
    private String voiceName;

    /**
     * 音色类型(1-预设音色 2-克隆音色)
     */
    @ExcelProperty(value = "音色类型")
    private String voiceType;

    /**
     * 音色代码(预设音色使用)
     */
    @ExcelProperty(value = "音色代码")
    private String voiceCode;

    /**
     * 音色模型地址(克隆音色使用)
     */
    @ExcelProperty(value = "音色模型地址")
    private String voiceModelUrl;

    /**
     * 样本音频地址
     */
    @ExcelProperty(value = "样本音频地址")
    private String sampleAudioUrl;

    /**
     * 音色配置参数
     */
    @ExcelProperty(value = "音色配置参数")
    private String voiceConfig;

    /**
     * 是否默认公开(0-否 1-是)
     */
    @ExcelProperty(value = "是否默认公开")
    private String isDefaultPublic;

    /**
     * 状态(0-禁用 1-启用 2-训练中)
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

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
