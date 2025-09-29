package org.ruoyi.digital.controller.chat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.core.domain.R;
import org.ruoyi.digital.request.DigitalHumanChatRequest;
import org.ruoyi.digital.request.DigitalHumanVoiceChatRequest;
import org.ruoyi.digital.service.IDigitalHumanChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 数字人聊天控制器
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/digital/chat")
@Tag(name = "数字人聊天管理", description = "数字人聊天相关接口")
public class DigitalHumanChatController {

    @Autowired
    private IDigitalHumanChatService digitalHumanChatService;

    /**
     * 数字人聊天接口（支持文本和语音输入）
     */
    @PostMapping("/send")
    @Operation(summary = "数字人聊天", description = "支持文本和语音输入，可配置文本或语音输出")
    public SseEmitter chat(@RequestBody @Valid DigitalHumanChatRequest request, HttpServletRequest httpRequest) {
        log.info("收到数字人聊天请求: digitalHumanId={}, messageType={}, outputType={}",
                request.getDigitalHumanId(), request.getMessageType(), request.getOutputType());
        
        return digitalHumanChatService.chat(request, httpRequest);
    }

    /**
     * 数字人实时语音通话接口
     */
    @PostMapping(value = "/voice", consumes = "multipart/form-data")
    @Operation(summary = "数字人语音通话", description = "实时语音通话，语音输入语音输出")
    public SseEmitter voiceChat(@ModelAttribute @Valid DigitalHumanVoiceChatRequest request, HttpServletRequest httpRequest) {
        log.info("收到数字人语音通话请求: digitalHumanId={}", request.getDigitalHumanId());
        
        return digitalHumanChatService.voiceChat(request, httpRequest);
    }
}
