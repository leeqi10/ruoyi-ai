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
import org.ruoyi.digital.human.domain.DigitalHumanMessage;
import org.ruoyi.digital.human.domain.bo.DigitalHumanMessageBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanMessageVo;
import org.ruoyi.digital.human.mapper.DigitalHumanMessageMapper;
import org.ruoyi.digital.human.service.IDigitalHumanMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 数字人消息服务实现类
 *
 * @author leeqi
 * @date 2025-09-26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DigitalHumanMessageServiceImpl implements IDigitalHumanMessageService {

    @Autowired
    private DigitalHumanMessageMapper baseMapper;

    @Override
    public TableDataInfo<DigitalHumanMessageVo> queryPageList(DigitalHumanMessageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DigitalHumanMessage> lqw = buildQueryWrapper(bo);
        Page<DigitalHumanMessageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<DigitalHumanMessageVo> queryList(DigitalHumanMessageBo bo) {
        LambdaQueryWrapper<DigitalHumanMessage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DigitalHumanMessageVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insertByBo(DigitalHumanMessageBo bo) {
        DigitalHumanMessage add = MapstructUtils.convert(bo, DigitalHumanMessage.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(DigitalHumanMessageBo bo) {
        DigitalHumanMessage update = MapstructUtils.convert(bo, DigitalHumanMessage.class);
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
    public List<DigitalHumanMessageVo> queryMessageHistory(Long sessionId, Integer limit) {
        LambdaQueryWrapper<DigitalHumanMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(DigitalHumanMessage::getSessionId, sessionId);
        lqw.orderByAsc(DigitalHumanMessage::getCreateTime);
        if (limit != null && limit > 0) {
            lqw.last("LIMIT " + limit);
        }
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public Integer countMessagesBySessionId(Long sessionId) {
        LambdaQueryWrapper<DigitalHumanMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(DigitalHumanMessage::getSessionId, sessionId);
        return Math.toIntExact(baseMapper.selectCount(lqw));
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DigitalHumanMessage entity) {
        // TODO 做一些数据校验，如唯一约束
    }

    private LambdaQueryWrapper<DigitalHumanMessage> buildQueryWrapper(DigitalHumanMessageBo bo) {
        LambdaQueryWrapper<DigitalHumanMessage> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSessionId() != null, DigitalHumanMessage::getSessionId, bo.getSessionId());
        lqw.eq(bo.getUserId() != null, DigitalHumanMessage::getUserId, bo.getUserId());
        lqw.eq(bo.getDigitalHumanId() != null, DigitalHumanMessage::getDigitalHumanId, bo.getDigitalHumanId());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), DigitalHumanMessage::getContent, bo.getContent());
        lqw.eq(StringUtils.isNotBlank(bo.getMessageType()), DigitalHumanMessage::getMessageType, bo.getMessageType());
        lqw.eq(StringUtils.isNotBlank(bo.getRole()), DigitalHumanMessage::getRole, bo.getRole());
        lqw.like(StringUtils.isNotBlank(bo.getModelName()), DigitalHumanMessage::getModelName, bo.getModelName());
        lqw.orderByAsc(DigitalHumanMessage::getCreateTime);
        return lqw;
    }
}
