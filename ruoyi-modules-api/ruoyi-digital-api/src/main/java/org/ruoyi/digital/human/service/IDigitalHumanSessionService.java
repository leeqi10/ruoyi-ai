package org.ruoyi.digital.human.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanSessionBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanSessionVo;

import java.util.Collection;
import java.util.List;

/**
 * 数字人会话服务接口
 *
 * @author ruoyi
 * @date 2025-09-24
 */
public interface IDigitalHumanSessionService {

    /**
     * 查询数字人会话信息列表
     */
    TableDataInfo<DigitalHumanSessionVo> queryPageList(DigitalHumanSessionBo bo, PageQuery pageQuery);

    /**
     * 查询数字人会话信息列表
     */
    List<DigitalHumanSessionVo> queryList(DigitalHumanSessionBo bo);

    /**
     * 查询数字人会话信息
     */
    DigitalHumanSessionVo queryById(Long id);

    /**
     * 新增数字人会话信息
     */
    Boolean insertByBo(DigitalHumanSessionBo bo);

    /**
     * 修改数字人会话信息
     */
    Boolean updateByBo(DigitalHumanSessionBo bo);

    /**
     * 校验并批量删除数字人会话信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 创建或获取会话
     * @param userId 用户ID
     * @param digitalHumanId 数字人ID
     * @param sessionType 会话类型(1-文字聊天 2-语音通话)
     * @param firstMessage 第一条消息（用作标题）
     * @return 会话信息
     */
    DigitalHumanSessionVo createOrGetSession(Long userId, Long digitalHumanId, String sessionType, String firstMessage,Long sessionId);

    /**
     * 更新会话信息
     * @param sessionId 会话ID
     * @param lastMessage 最后一条消息
     * @param messageCount 消息数量
     * @param duration 通话时长（可选）
     */
    Boolean updateSessionInfo(Long sessionId, String lastMessage, Integer messageCount, Integer duration);

}
