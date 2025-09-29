package org.ruoyi.digital.controller.list;

import lombok.RequiredArgsConstructor;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanSessionBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanSessionVo;
import org.ruoyi.digital.human.service.IDigitalHumanSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数字人会话信息
 *
 * @author leeqi
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/digital/session")
public class DigitalHumanSessionController {
    @Autowired
    private IDigitalHumanSessionService digitalHumanSessionService;

    /**
     * 查询数字人会话信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<DigitalHumanSessionVo> list(DigitalHumanSessionBo bo, PageQuery pageQuery) {
        return digitalHumanSessionService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询数字人会话信息详情
     */
    @GetMapping("/detail/{id}")
    public DigitalHumanSessionVo detail(@PathVariable("id") Long id) {
        return digitalHumanSessionService.queryById(id);
    }

    /**
     * 查询数字人会话信息列表（不分页）
     */
    @GetMapping("/all")
    public List<DigitalHumanSessionVo> all(DigitalHumanSessionBo bo) {
        return digitalHumanSessionService.queryList(bo);
    }
}
