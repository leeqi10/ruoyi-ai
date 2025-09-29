package org.ruoyi.digital.human.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 数字人信息对象 digital_human
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("digital_human")
public class DigitalHuman extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 数字人ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 创建用户ID
     */
    private Long userId;

    /**
     * 数字人名称
     */
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
