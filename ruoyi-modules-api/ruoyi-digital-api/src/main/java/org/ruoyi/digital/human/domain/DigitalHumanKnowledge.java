package org.ruoyi.digital.human.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ruoyi.core.domain.BaseEntity;

import java.io.Serial;

/**
 * 数字人知识库关联对象 digital_human_knowledge
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("digital_human_knowledge")
public class DigitalHumanKnowledge extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 数字人ID
     */
    private Long digitalHumanId;

    /**
     * 知识库ID
     */
    private Long knowledgeId;

    /**
     * 租户编号
     */
    private String tenantId;
}
