package org.ruoyi.digital.human.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.common.core.validate.AddGroup;
import org.ruoyi.common.core.validate.EditGroup;
import org.ruoyi.core.domain.BaseEntity;
import org.ruoyi.digital.human.domain.DigitalHumanVoice;

/**
 * 数字人音色业务对象 digital_human_voice
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DigitalHumanVoice.class, reverseConvertGenerate = false)
public class DigitalHumanVoiceBo extends BaseEntity {

    /**
     * 数字人id
     */
    private Long digitalHumanId;
    /**
     * 音色id
     */
    private Long voiceId;
    /**
     * 音色名称
     */
    private String voiceName;
}
