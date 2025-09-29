package org.ruoyi.digital.human.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.digital.human.domain.DigitalHuman;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 数字人信息视图对象 digital_human
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DigitalHuman.class)
public class DigitalHumanVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数字人ID
     */
    @ExcelProperty(value = "数字人ID")
    private Long id;

    /**
     * 创建用户ID
     */
    @ExcelProperty(value = "创建用户ID")
    private Long userId;

    /**
     * 数字人名称
     */
    @ExcelProperty(value = "数字人名称")
    private String name;

    /**
     * 头像地址
     */
    @ExcelProperty(value = "头像地址")
    private String avatar;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 人格类型(MBTI)
     */
    @ExcelProperty(value = "人格类型")
    private String personalityType;

    /**
     * 系统提示词
     */
    @ExcelProperty(value = "系统提示词")
    private String systemPrompt;

    /**
     * 音色ID
     */
    @ExcelProperty(value = "音色ID")
    private String voiceId;

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
     * 音色配置参数
     */
    @ExcelProperty(value = "音色配置参数")
    private String voiceConfig;

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

    /**
     * 关联的知识库列表
     */
    private List<DigitalHumanKnowledgeVo> knowledgeList;

    /**
     * 知识库数量
     */
    private Integer knowledgeCount;
}
