package org.ruoyi.digital.controller.list;

import lombok.RequiredArgsConstructor;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanBo;
import org.ruoyi.digital.human.service.IDigitalHumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;

import java.util.List;

/**
 * 数字人列表查询信息
 *
 * @author leeqi
 * @date 2025-09-25
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/digital")
public class DigitalHumanController {
    @Autowired
    private IDigitalHumanService digitalHumanService;
    /**
     * 查询接口
     */
    @GetMapping("/list")
    public TableDataInfo<DigitalHumanVo> list(DigitalHumanBo bo, PageQuery pageQuery) {
        return digitalHumanService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询数字人详情
     * 根据ID查询数字人详情，并记录用户访问历史到Redis
     */
    @GetMapping("/detail/{id}")
    public DigitalHumanVo detail(@PathVariable("id") Long id) {
        return digitalHumanService.queryDetailById(id);
    }

    /**
     * 查询用户最近访问的数字人列表
     * 从Redis中获取用户最近访问的数字人ID列表，并返回对应的数字人信息
     */
    @GetMapping("/recent")
    public List<DigitalHumanVo> recentAccess(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        Long userId = LoginHelper.getUserId();
        return digitalHumanService.queryRecentAccessList(userId, limit);
    }
}
