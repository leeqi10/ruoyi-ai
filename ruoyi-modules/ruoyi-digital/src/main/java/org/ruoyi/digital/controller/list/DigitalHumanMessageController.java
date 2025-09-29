package org.ruoyi.digital.controller.list;

import lombok.RequiredArgsConstructor;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanMessageBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanMessageVo;
import org.ruoyi.digital.human.service.IDigitalHumanMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数字人聊天信息
 *
 * @author leeqi
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/digital/message")
public class DigitalHumanMessageController {

    @Autowired
    private IDigitalHumanMessageService digitalHumanMessageService;

    /**
     * 查询数字人消息信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<DigitalHumanMessageVo> list(DigitalHumanMessageBo bo, PageQuery pageQuery) {
        return digitalHumanMessageService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询数字人消息信息详情
     */
    @GetMapping("/detail/{id}")
    public DigitalHumanMessageVo detail(@PathVariable("id") Long id) {
        return digitalHumanMessageService.queryById(id);
    }

    /**
     * 查询数字人消息信息列表（不分页）
     */
    @GetMapping("/all")
    public List<DigitalHumanMessageVo> all(DigitalHumanMessageBo bo) {
        return digitalHumanMessageService.queryList(bo);
    }
}
