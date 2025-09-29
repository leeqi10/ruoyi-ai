package org.ruoyi.digital.human.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;

import java.util.Collection;
import java.util.List;

/**
 * 数字人信息Service接口
 *
 * @author ruoyi
 * @date 2025-09-24
 */
public interface IDigitalHumanService {

    /**
     * 查询数字人信息
     */
    DigitalHumanVo queryById(Long id);

    /**
     * 查询数字人信息列表
     */
    TableDataInfo<DigitalHumanVo> queryPageList(DigitalHumanBo bo, PageQuery pageQuery);

    /**
     * 查询数字人信息列表
     */
    List<DigitalHumanVo> queryList(DigitalHumanBo bo);

    /**
     * 查询用户最近使用的数字人列表
     */
    List<DigitalHumanVo> queryRecentList(Long userId, Integer limit);

    /**
     * 查询公开的数字人市场列表
     */
    TableDataInfo<DigitalHumanVo> queryMarketList(DigitalHumanBo bo, PageQuery pageQuery);

    /**
     * 新增数字人信息
     */
    Boolean insertByBo(DigitalHumanBo bo);

    /**
     * 修改数字人信息
     */
    Boolean updateByBo(DigitalHumanBo bo);

    /**
     * 校验并批量删除数字人信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 导出数字人配置
     */
    String exportDigitalHuman(Long id);

    /**
     * 克隆数字人
     */
    Boolean cloneDigitalHuman(Long id, String name);

    /**
     * 查询数字人详情并记录访问历史
     */
    DigitalHumanVo queryDetailById(Long id);

    /**
     * 查询用户最近访问的数字人列表
     */
    List<DigitalHumanVo> queryRecentAccessList(Long userId, Integer limit);
    /**
     * 修改数字人配置
     */
    Boolean updateByHumanIdAndVoiceId(DigitalHumanBo bo);
}
