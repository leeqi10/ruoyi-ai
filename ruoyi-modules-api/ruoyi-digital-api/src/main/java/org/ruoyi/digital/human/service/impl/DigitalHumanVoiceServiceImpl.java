package org.ruoyi.digital.human.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.DigitalHuman;
import org.ruoyi.digital.human.domain.DigitalHumanVoice;
import org.ruoyi.digital.human.domain.bo.DigitalHumanBo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanVoiceBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVoiceVo;
import org.ruoyi.digital.human.mapper.DigitalHumanVoiceMapper;
import org.ruoyi.digital.human.service.IDigitalHumanVoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数字人音色服务实现类
 *
 * @author ruoyi
 * @date 2025-09-24
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DigitalHumanVoiceServiceImpl implements IDigitalHumanVoiceService {
    @Autowired
    private DigitalHumanVoiceMapper digitalHumanVoiceMapper;

    @Override
    public DigitalHumanVoiceVo queryById(Long id) {
        return digitalHumanVoiceMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<DigitalHumanVoiceVo> queryPageList(DigitalHumanVoiceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DigitalHumanVoice> lqw = buildQueryWrapper(bo);
        Page<DigitalHumanVoiceVo> result = digitalHumanVoiceMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
    private LambdaQueryWrapper<DigitalHumanVoice> buildQueryWrapper(DigitalHumanVoiceBo bo) {
        LambdaQueryWrapper<DigitalHumanVoice> lqw = Wrappers.lambdaQuery();
        // 对声音类型进行排序和可以进行搜索
        lqw.orderByDesc(DigitalHumanVoice::getVoiceType)
                .eq(DigitalHumanVoice::getStatus,1)
                .eq(DigitalHumanVoice::getIsDefaultPublic,1)
                .like(StringUtils.isNotBlank(bo.getVoiceName()), DigitalHumanVoice::getVoiceName, bo.getVoiceName());
        return lqw;
    }

    @Override
    public List<DigitalHumanVoiceVo> queryPresetVoices() {
        return List.of();
    }

    @Override
    public List<DigitalHumanVoiceVo> queryUserVoices(Long userId) {
        return List.of();
    }

    @Override
    public Boolean insertByBo(DigitalHumanVoiceBo bo) {
        return null;
    }

    @Override
    public Boolean updateByBo(DigitalHumanVoiceBo bo) {
        return null;
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Override
    public DigitalHumanVoiceVo cloneVoice(String voiceName, MultipartFile sampleAudio) {
        return null;
    }

    @Override
    public String getCloneStatus(Long voiceId) {
        return "";
    }

    @Override
    public byte[] testVoice(Long voiceId, String text) {
        return new byte[0];
    }
}
