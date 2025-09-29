package org.ruoyi.digital.controller.list;

import lombok.RequiredArgsConstructor;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.DigitalHumanKnowledge;
import org.ruoyi.digital.human.domain.bo.DigitalHumanBo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanVoiceBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanKnowledgeVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVoiceVo;
import org.ruoyi.digital.human.service.IDigitalHumanKnowledgeService;
import org.ruoyi.digital.human.service.IDigitalHumanService;
import org.ruoyi.digital.human.service.IDigitalHumanVoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数字人详情信息
 *
 * @author leeqi
 * @date 2025-09-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/digital/detail")
public class DigitalHumanDetailController {
    @Autowired
    private IDigitalHumanVoiceService digitalHumanVoiceService;
    @Autowired
    private IDigitalHumanService digitalHumanService;
    @Autowired
    private IDigitalHumanKnowledgeService digitalHumanKnowledgeService;
    /**
     * 查询声音选择的列表接口
     */
    @GetMapping("/voice/list")
    public TableDataInfo<DigitalHumanVoiceVo> list(DigitalHumanVoiceBo bo, PageQuery pageQuery) {
        return digitalHumanVoiceService.queryPageList(bo, pageQuery);
    }
    /**
     * 声音详情接口
     */
    @GetMapping("/voice/detail/{id}")
    public DigitalHumanVoiceVo detail(@PathVariable("id") Long id) {
        return digitalHumanVoiceService.queryById(id);
    }
    /**
     * 更新用户声音信息
     * 只需要更新声音选择的Id
     */
    @PutMapping("/voice/update/digital")
    public Boolean updateDigital(@RequestBody DigitalHumanBo bo) {
        return digitalHumanService.updateByHumanIdAndVoiceId(bo);
    }


}
