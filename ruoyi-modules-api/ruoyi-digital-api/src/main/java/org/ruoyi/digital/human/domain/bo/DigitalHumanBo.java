package org.ruoyi.digital.human.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.digital.human.domain.DigitalHuman;

import java.util.List;

/**
 * 数字人信息业务对象 digital_human
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DigitalHuman.class, reverseConvertGenerate = false)
public class DigitalHumanBo extends BaseEntity {

    /**
     * 数字人ID
     */
    @NotNull(message = "数字人ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 创建用户ID
     */
    private Long userId;

    /**
     * 数字人名称
     */
    @NotBlank(message = "数字人名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 描述
     */
    private String description;

    /**
     * 人格类型(MBTI)
     */
    private String personalityType;

    /**
     * 系统提示词
     */
    private String systemPrompt;

    /**
     * 音色ID
     */
    private String voiceId;


    /**
     * 关联的知识库ID列表
     */
    private List<Long> knowledgeIds;
}
