package org.ruoyi.digital.human.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.digital.constant.RedisKeyConst;
import org.ruoyi.common.redis.utils.RedisUtils;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.DigitalHuman;
import org.ruoyi.digital.human.domain.DigitalHumanKnowledge;
import org.ruoyi.digital.human.domain.bo.DigitalHumanBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanKnowledgeVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;
import org.ruoyi.digital.human.mapper.DigitalHumanKnowledgeMapper;
import org.ruoyi.digital.human.mapper.DigitalHumanMapper;
import org.ruoyi.digital.human.service.IDigitalHumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数字人信息Service业务层处理
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@RequiredArgsConstructor
@Service
public class DigitalHumanServiceImpl implements IDigitalHumanService {
    @Autowired
    private DigitalHumanMapper baseMapper;
    @Autowired
    private DigitalHumanKnowledgeMapper digitalHumanKnowledgeMapper;
    @Override
    public DigitalHumanVo queryById(Long id) {
        return queryDetailById(id);
    }

    @Override
    public TableDataInfo<DigitalHumanVo> queryPageList(DigitalHumanBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DigitalHuman> lqw = buildQueryWrapper(bo);
        Page<DigitalHumanVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<DigitalHuman> buildQueryWrapper(DigitalHumanBo bo) {
        LambdaQueryWrapper<DigitalHuman> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), DigitalHuman::getName, bo.getName());
        return lqw;
    }

    @Override
    public List<DigitalHumanVo> queryList(DigitalHumanBo bo) {
        return List.of();
    }

    @Override
    public List<DigitalHumanVo> queryRecentList(Long userId, Integer limit) {
        return List.of();
    }

    @Override
    public TableDataInfo<DigitalHumanVo> queryMarketList(DigitalHumanBo bo, PageQuery pageQuery) {
        return null;
    }

    @Override
    public Boolean insertByBo(DigitalHumanBo bo) {
        return null;
    }

    @Override
    public Boolean updateByBo(DigitalHumanBo bo) {
        return null;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return null;
    }

    @Override
    public String exportDigitalHuman(Long id) {
        return "";
    }

    @Override
    public Boolean cloneDigitalHuman(Long id, String name) {
        return null;
    }

    @Override
    public DigitalHumanVo queryDetailById(Long id) {
        DigitalHumanVo digitalHumanVo = baseMapper.selectVoById(id);
        if (digitalHumanVo == null) {
            return null;
        }

        // 查询关联的知识库表
        LambdaQueryWrapper<DigitalHumanKnowledge> lqw = Wrappers.lambdaQuery();
        lqw.eq(DigitalHumanKnowledge::getDigitalHumanId, digitalHumanVo.getId());
        List<DigitalHumanKnowledgeVo> digitalHumanKnowledgeVos = digitalHumanKnowledgeMapper.selectVoList(lqw);
        if (digitalHumanKnowledgeVos != null) {
            digitalHumanVo.setKnowledgeList(digitalHumanKnowledgeVos);
            digitalHumanVo.setKnowledgeCount(digitalHumanKnowledgeVos.size());

        }
        Long userId = LoginHelper.getUserId();
        if (userId != null) {
            String redisKey = String.format(RedisKeyConst.RECENT_ACCESS_LIST, userId);

            // 使用原生 Redis List：先移除已有的，再插入到最前面
            RList<Long> rList = RedisUtils.CLIENT.getList(redisKey);
            rList.remove(id);
            rList.add(0, id);

            // 截断只保留前 10 个
            if (rList.size() > 10) {
                rList.trim(0, 9); // 保留索引 0~9 共 10 个
            }

            // 设置过期时间 7 天
            rList.expire(Duration.ofDays(7));
        }

        return digitalHumanVo;
    }
    @Override
    public List<DigitalHumanVo> queryRecentAccessList(Long userId, Integer limit) {
        if (userId == null) {
            return Collections.emptyList();
        }

        String redisKey = String.format(RedisKeyConst.RECENT_ACCESS_LIST, userId);
        RList<Long> rList = RedisUtils.CLIENT.getList(redisKey);
        if (rList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> recentIds = (limit != null && limit > 0)
                ? rList.readAll().stream().limit(limit).toList()
                : rList.readAll();

        if (recentIds.isEmpty()) {
            return Collections.emptyList();
        }

        LambdaQueryWrapper<DigitalHuman> lqw = Wrappers.lambdaQuery();
        lqw.in(DigitalHuman::getId, recentIds);
        List<DigitalHumanVo> digitalHumanVos = baseMapper.selectVoList(lqw);

        // 保持访问顺序
        Map<Long, DigitalHumanVo> voMap = digitalHumanVos.stream()
                .collect(Collectors.toMap(DigitalHumanVo::getId, vo -> vo));
        return recentIds.stream()
                .map(voMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean updateByHumanIdAndVoiceId(DigitalHumanBo bo) {
        // 获取数据
        DigitalHuman digitalHuman = baseMapper.selectById(bo.getId());
        if (digitalHuman == null) {
            return false;
        }
        digitalHuman.setId(bo.getId());
        digitalHuman.setVoiceId(bo.getVoiceId());
        return baseMapper.updateById(digitalHuman) > 0;
    }
}
