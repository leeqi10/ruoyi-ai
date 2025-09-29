package org.ruoyi.digital.service;

import jakarta.servlet.http.HttpServletRequest;
import org.ruoyi.digital.request.DigitalHumanChatRequest;
import org.ruoyi.digital.request.DigitalHumanVoiceChatRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 数字人聊天服务接口
 *
 * @author leeqi
 * @date 2025-09-28
 */
public interface IDigitalHumanChatService {

    /**
     * 数字人聊天接口
     * @param request 聊天请求
     * @param httpRequest HTTP请求
     * @return SSE流式响应
     */
    SseEmitter chat(DigitalHumanChatRequest request, HttpServletRequest httpRequest);

    /**
     * 数字人实时语音通话接口
     * @param request 语音聊天请求
     * @param httpRequest HTTP请求
     * @return SSE流式响应
     */
    SseEmitter voiceChat(DigitalHumanVoiceChatRequest request, HttpServletRequest httpRequest);

}
