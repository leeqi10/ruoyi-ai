package org.ruoyi.digital.human.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.MapstructUtils;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.DigitalHumanSession;
import org.ruoyi.digital.human.domain.bo.DigitalHumanSessionBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanSessionVo;
import org.ruoyi.digital.human.mapper.DigitalHumanSessionMapper;
import org.ruoyi.digital.human.service.IDigitalHumanSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 数字人会话服务实现类
 *
 * @author leeqi
 * @date 2025-09-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DigitalHumanSessionServiceImpl implements IDigitalHumanSessionService {

    @Autowired
    private DigitalHumanSessionMapper baseMapper;

    @Override
    public TableDataInfo<DigitalHumanSessionVo> queryPageList(DigitalHumanSessionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DigitalHumanSession> lqw = buildQueryWrapper(bo);
        Page<DigitalHumanSessionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<DigitalHumanSessionVo> queryList(DigitalHumanSessionBo bo) {
        LambdaQueryWrapper<DigitalHumanSession> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DigitalHumanSessionVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insertByBo(DigitalHumanSessionBo bo) {
        DigitalHumanSession add = MapstructUtils.convert(bo, DigitalHumanSession.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(DigitalHumanSessionBo bo) {
        DigitalHumanSession update = MapstructUtils.convert(bo, DigitalHumanSession.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验，判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public DigitalHumanSessionVo createOrGetSession(Long userId, Long digitalHumanId, String sessionType, String firstMessage,Long sessionId) {
        // 检查会话是否存在
        DigitalHumanSessionVo session = queryById(sessionId);
        if (session != null) {
            return session;
        }
        // 创建新会话
        DigitalHumanSessionBo newSessionBo = new DigitalHumanSessionBo();
        newSessionBo.setUserId(userId);
        newSessionBo.setDigitalHumanId(digitalHumanId);
        newSessionBo.setSessionType(sessionType);
        newSessionBo.setTitle(generateSessionTitle(firstMessage));
        newSessionBo.setLastMessage("");
        newSessionBo.setMessageCount(0);
        newSessionBo.setDuration(0);
        newSessionBo.setStatus("1"); // 1-进行中
        
        if (insertByBo(newSessionBo)) {
            return queryById(newSessionBo.getId());
        }
        
        throw new RuntimeException("创建数字人会话失败");
    }

    @Override
    public Boolean updateSessionInfo(Long sessionId, String lastMessage, Integer messageCount, Integer duration) {
        DigitalHumanSessionBo updateBo = new DigitalHumanSessionBo();
        updateBo.setId(sessionId);
        updateBo.setLastMessage(lastMessage);
        updateBo.setMessageCount(messageCount);
        if (duration != null) {
            updateBo.setDuration(duration);
        }
        return updateByBo(updateBo);
    }

    /**
     * 生成会话标题（从第一条消息中提取）
     */
    private String generateSessionTitle(String firstMessage) {
        if (StringUtils.isBlank(firstMessage)) {
            return "新对话";
        }
        // 截取前30个字符作为标题
        return firstMessage.length() > 30 ? firstMessage.substring(0, 30) + "..." : firstMessage;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DigitalHumanSession entity) {
        // TODO 做一些数据校验，如唯一约束
    }

    private LambdaQueryWrapper<DigitalHumanSession> buildQueryWrapper(DigitalHumanSessionBo bo) {
        LambdaQueryWrapper<DigitalHumanSession> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, DigitalHumanSession::getUserId, bo.getUserId());
        lqw.eq(bo.getDigitalHumanId() != null, DigitalHumanSession::getDigitalHumanId, bo.getDigitalHumanId());
        lqw.eq(StringUtils.isNotBlank(bo.getSessionType()), DigitalHumanSession::getSessionType, bo.getSessionType());
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), DigitalHumanSession::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), DigitalHumanSession::getStatus, bo.getStatus());
        lqw.orderByDesc(DigitalHumanSession::getCreateTime);
        return lqw;
    }
}
