package org.ruoyi.digital.human.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 数字人音色对象 digital_human_voice
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("digital_human_voice")
public class DigitalHumanVoice extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 音色ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 音色名称
     */
    private String voiceName;

    /**
     * 音色类型(1-预设音色 2-克隆音色)
     */
    private String voiceType;

    /**
     * 音色代码(预设音色使用)
     */
    private String voiceCode;

    /**
     * 音色模型地址(克隆音色使用)
     */
    private String voiceModelUrl;

    /**
     * 样本音频地址
     */
    private String sampleAudioUrl;

    /**
     * 音色配置参数
     */
    private String voiceConfig;

    /**
     * 是否默认公开(0-否 1-是)
     */
    private String isDefaultPublic;

    /**
     * 状态(0-禁用 1-启用 2-训练中)
     */
    private String status;

    /**
     * 租户编号
     */
    private String tenantId;
}
