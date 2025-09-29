package org.ruoyi.digital.human.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.ruoyi.digital.human.domain.DigitalHumanKnowledge;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 数字人知识库关联视图对象 digital_human_knowledge
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DigitalHumanKnowledge.class)
public class DigitalHumanKnowledgeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 数字人ID
     */
    @ExcelProperty(value = "数字人ID")
    private Long digitalHumanId;

    /**
     * 知识库ID
     */
    @ExcelProperty(value = "知识库ID")
    private Long knowledgeId;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
