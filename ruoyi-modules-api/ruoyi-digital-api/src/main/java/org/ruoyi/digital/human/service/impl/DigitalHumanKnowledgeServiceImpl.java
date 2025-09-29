package org.ruoyi.digital.human.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.chat.entity.Tts.TextToSpeech;
import org.ruoyi.common.chat.entity.whisper.WhisperResponse;
import org.ruoyi.core.page.PageQuery;
import org.ruoyi.core.page.TableDataInfo;
import org.ruoyi.digital.human.domain.DigitalHuman;
import org.ruoyi.digital.human.domain.DigitalHumanKnowledge;
import org.ruoyi.digital.human.domain.vo.DigitalHumanKnowledgeVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanMessageVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanSessionVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;
import org.ruoyi.digital.human.mapper.DigitalHumanKnowledgeMapper;
import org.ruoyi.digital.human.service.IDigitalHumanKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 数字人关联知识库服务实现类
 *
 * @author leeqi
 * @date 2025-09-27
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DigitalHumanKnowledgeServiceImpl implements IDigitalHumanKnowledgeService {

}
