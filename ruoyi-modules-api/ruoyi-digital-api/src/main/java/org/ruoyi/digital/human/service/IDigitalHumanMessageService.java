package org.ruoyi.digital.human.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanMessageBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanMessageVo;

import java.util.Collection;
import java.util.List;

/**
 * 数字人消息服务接口
 *
 * @author ruoyi
 * @date 2025-09-24
 */
public interface IDigitalHumanMessageService {

    /**
     * 查询数字人消息信息列表
     */
    TableDataInfo<DigitalHumanMessageVo> queryPageList(DigitalHumanMessageBo bo, PageQuery pageQuery);

    /**
     * 查询数字人消息信息列表
     */
    List<DigitalHumanMessageVo> queryList(DigitalHumanMessageBo bo);

    /**
     * 查询数字人消息信息
     */
    DigitalHumanMessageVo queryById(Long id);

    /**
     * 新增数字人消息信息
     */
    Boolean insertByBo(DigitalHumanMessageBo bo);

    /**
     * 修改数字人消息信息
     */
    Boolean updateByBo(DigitalHumanMessageBo bo);

    /**
     * 校验并批量删除数字人消息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据会话ID查询消息历史（按时间正序）
     */
    List<DigitalHumanMessageVo> queryMessageHistory(Long sessionId, Integer limit);

    /**
     * 根据会话ID统计消息数量
     */
    Integer countMessagesBySessionId(Long sessionId);

}
