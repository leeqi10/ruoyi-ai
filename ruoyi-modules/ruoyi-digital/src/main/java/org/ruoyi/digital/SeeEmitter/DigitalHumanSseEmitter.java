package org.ruoyi.digital.SeeEmitter;

import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.digital.service.XfyunTextToSpeechService;
import org.ruoyi.digital.human.domain.bo.DigitalHumanMessageBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanSessionVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;
import org.ruoyi.digital.human.service.IDigitalHumanMessageService;
import org.ruoyi.digital.human.service.IDigitalHumanSessionService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

/**
 * 数字人SSE发射器包装器
 */
@Slf4j
public   class DigitalHumanSseEmitter extends SseEmitter {
    private final SseEmitter delegate;
    private final DigitalHumanSessionVo session;
    private final DigitalHumanVo digitalHuman;
    private final String outputType;
    private final IDigitalHumanMessageService messageService;
    private final IDigitalHumanSessionService sessionService;
    private final StringBuilder responseBuilder = new StringBuilder();
    private final XfyunTextToSpeechService textToSpeechService ;

    public DigitalHumanSseEmitter(SseEmitter delegate, DigitalHumanSessionVo session, DigitalHumanVo digitalHuman, String outputType,
                                  IDigitalHumanMessageService messageService, IDigitalHumanSessionService sessionService,XfyunTextToSpeechService textToSpeechService) {
        super(delegate.getTimeout());
        this.delegate = delegate;
        this.session = session;
        this.digitalHuman = digitalHuman;
        this.outputType = outputType;
        this.messageService = messageService;
        this.sessionService = sessionService;
        this.textToSpeechService = textToSpeechService;
    }

    @Override
    public void send(Object object) throws IOException {
        delegate.send(object);

        // 收集AI回复内容
        if (object instanceof String content) {
            responseBuilder.append(content);
        }
    }

    @Override
    public void complete() {
        try {
            String aiResponse = responseBuilder.toString();
            if (StringUtils.isNotBlank(aiResponse)) {
                // 保存AI回复消息
                saveAiMessage(aiResponse);

                // 更新会话信息
                updateSessionInfo(aiResponse);
            }

            delegate.complete();
        } catch (Exception e) {
            log.error("保存数字人回复失败", e);
            delegate.completeWithError(e);
        }
    }

    @Override
    public void completeWithError(Throwable ex) {
        delegate.completeWithError(ex);
    }

    private void saveAiMessage(String content) {
        DigitalHumanMessageBo messageBo = new DigitalHumanMessageBo();
        messageBo.setSessionId(session.getId());
        messageBo.setUserId(session.getUserId());
        messageBo.setDigitalHumanId(session.getDigitalHumanId());
        messageBo.setContent(content);
        messageBo.setMessageType("1"); // 文本消息
        messageBo.setRole("assistant");

        // 如果需要语音输出，调用文字转语音服务
        if ("2".equals(outputType)) {
            try {
                String audioUrl = convertTextToSpeech(content);
                messageBo.setAudioUrl(audioUrl);
            } catch (Exception e) {
                log.error("文字转语音失败", e);
                // 文字转语音失败不影响文本消息的保存
            }
        }

        messageService.insertByBo(messageBo);
    }

    private void updateSessionInfo(String lastMessage) {
        Integer messageCount = messageService.countMessagesBySessionId(session.getId());
        sessionService.updateSessionInfo(session.getId(), lastMessage, messageCount, null);
    }
    /**
     * 文字转语音
     */
    private String convertTextToSpeech(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        try {
            log.info("开始文字转语音，文本长度: {}", text.length());
            String audioUrl = textToSpeechService.convertTextToSpeech(text);
            log.info("文字转语音成功，音频URL: {}", audioUrl);
            return audioUrl;
        } catch (Exception e) {
            log.error("文字转语音失败，文本: {}", text, e);
            throw e;
        }
    }

}