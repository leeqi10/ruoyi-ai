package org.ruoyi.digital.human.service;

import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.bo.DigitalHumanVoiceBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVoiceVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数字人音色服务接口
 *
 * @author ruoyi
 * @date 2025-09-24
 */
public interface IDigitalHumanVoiceService {

    /**
     * 查询音色信息
     */
    DigitalHumanVoiceVo queryById(Long id);

    /**
     * 查询音色信息列表
     */
    TableDataInfo<DigitalHumanVoiceVo> queryPageList(DigitalHumanVoiceBo bo, PageQuery pageQuery);

    /**
     * 查询预设音色列表
     */
    List<DigitalHumanVoiceVo> queryPresetVoices();

    /**
     * 查询用户自定义音色列表
     */
    List<DigitalHumanVoiceVo> queryUserVoices(Long userId);

    /**
     * 新增音色信息
     */
    Boolean insertByBo(DigitalHumanVoiceBo bo);

    /**
     * 修改音色信息
     */
    Boolean updateByBo(DigitalHumanVoiceBo bo);

    /**
     * 删除音色信息
     */
    Boolean deleteById(Long id);

    /**
     * 克隆音色
     */
    DigitalHumanVoiceVo cloneVoice(String voiceName, MultipartFile sampleAudio);

    /**
     * 获取音色克隆状态
     */
    String getCloneStatus(Long voiceId);

    /**
     * 测试音色
     */
    byte[] testVoice(Long voiceId, String text);
}
