package org.ruoyi.digital.service.impl;

import io.reactivex.Single;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ruoyi.chat.service.chat.ISseService;
import org.ruoyi.chat.service.chat.impl.DeepSeekChatImpl;
import org.ruoyi.common.chat.entity.chat.Message;
import org.ruoyi.common.chat.request.ChatRequest;
import org.ruoyi.common.core.utils.StringUtils;
import org.ruoyi.common.digital.service.XfyunVoiceRecognitionService;
import org.ruoyi.common.digital.service.XfyunTextToSpeechService;
import org.ruoyi.common.satoken.utils.LoginHelper;
import org.ruoyi.digital.SeeEmitter.DigitalHumanSseEmitter;
import org.ruoyi.digital.human.domain.vo.DigitalHumanKnowledgeVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanMessageVo;
import org.ruoyi.digital.request.DigitalHumanChatRequest;
import org.ruoyi.digital.request.DigitalHumanVoiceChatRequest;
import org.ruoyi.digital.service.IDigitalHumanChatService;
import org.ruoyi.digital.human.domain.bo.DigitalHumanMessageBo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanSessionVo;
import org.ruoyi.digital.human.domain.vo.DigitalHumanVo;
import org.ruoyi.digital.human.service.IDigitalHumanMessageService;
import org.ruoyi.digital.human.service.IDigitalHumanService;
import org.ruoyi.digital.human.service.IDigitalHumanSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数字人聊天服务实现类
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DigitalHumanChatServiceImpl implements IDigitalHumanChatService {

    @Autowired
    private IDigitalHumanService digitalHumanService;
    
    @Autowired
    private IDigitalHumanSessionService digitalHumanSessionService;
    
    @Autowired
    private IDigitalHumanMessageService digitalHumanMessageService;

    @Autowired
    private XfyunVoiceRecognitionService voiceRecognitionService;

    @Autowired
    private XfyunTextToSpeechService textToSpeechService;
    @Autowired
    private ISseService sseService;
    @Autowired
    private DeepSeekChatImpl chatService;

    @Value("${ruoyi.profile:/tmp/ruoyi-uploads}")
    private String uploadPath;

    @Override
    public SseEmitter chat(DigitalHumanChatRequest request, HttpServletRequest httpRequest) {
        log.info("数字人聊天请求: {}", request);
        
        try {
            // 获取当前用户ID
            Long userId = LoginHelper.getUserId();
            // 处理用户输入消息
            String userMessage = request.getContent();
            // 获取数字人信息
            DigitalHumanVo digitalHuman = digitalHumanService.queryById(request.getDigitalHumanId());
            if (digitalHuman == null) {
                throw new RuntimeException("数字人不存在");
            }
            // 如果是语音消息，先转换为文本
            if ("2".equals(request.getMessageType()) && StringUtils.isBlank(userMessage)) {
                userMessage = processVoiceInput(request);
            }
            
            if (StringUtils.isBlank(userMessage)) {
                throw new RuntimeException("用户消息不能为空");
            }
            
            // 创建或获取会话
            DigitalHumanSessionVo session = digitalHumanSessionService.createOrGetSession(
                userId, 
                request.getDigitalHumanId(), 
                request.getSessionType(), 
                userMessage,
                    request.getSessionId()
            );

            // 构建聊天请求
            ChatRequest chatRequest = buildChatRequest(digitalHuman, session, userMessage, userId,request.getModel());
            // 保存用户消息
            saveUserMessage(session.getId(), userId, request.getDigitalHumanId(), userMessage, request.getMessageType());

            SseEmitter sseEmitter = sseService.sseChat(chatRequest, httpRequest);

            // 包装SSE发射器以保存消息和更新会话
            wrapSseEmitterForDigitalHuman(sseEmitter, session, digitalHuman, request.getOutputType());
            return chatService.chat(chatRequest, sseEmitter);

        } catch (Exception e) {
            log.error("数字人聊天失败", e);
            SseEmitter errorEmitter = new SseEmitter();
            try {
                errorEmitter.send("error: " + e.getMessage());
                errorEmitter.complete();
            } catch (IOException ioException) {
                log.error("发送错误消息失败", ioException);
            }
            return errorEmitter;
        }
    }

    @Override
    public SseEmitter voiceChat(DigitalHumanVoiceChatRequest request, HttpServletRequest httpRequest) {
        log.info("数字人实时语音通话请求: {}", request);
        
        // 将语音聊天请求转换为普通聊天请求
        DigitalHumanChatRequest chatRequest = new DigitalHumanChatRequest();
        chatRequest.setSessionId(request.getSessionId());
        chatRequest.setDigitalHumanId(request.getDigitalHumanId());
        chatRequest.setContent(request.getContent());
        chatRequest.setMessageType(request.getMessageType());
        chatRequest.setSessionType("2"); // 语音通话
        chatRequest.setAudioFile(request.getAudioFile());
        chatRequest.setModel(request.getModel());
        chatRequest.setOutputType(request.getOutputType());
        
        return chat(chatRequest, httpRequest);
    }

    /**
     * 处理语音输入，转换为文本
     */
    private String processVoiceInput(DigitalHumanChatRequest request) {
        try {
            MultipartFile audioFile = request.getAudioFile();
            
            if (audioFile != null && !audioFile.isEmpty()) {
                // 通过文件进行语音识别
                return recognizeAudioFromFile(audioFile);
            } else {
                throw new RuntimeException("语音消息缺少音频文件或URL");
            }
            
        } catch (Exception e) {
            log.error("语音转文字失败", e);
            throw new RuntimeException("语音转文字失败: " + e.getMessage());
        }
    }

    /**
     * 通过文件进行语音识别
     */
    private String recognizeAudioFromFile(MultipartFile audioFile) {
        try {
            log.info("开始语音识别，文件名: {}, 文件大小: {} bytes", audioFile.getOriginalFilename(), audioFile.getSize());
            
            // 获取音频数据
            byte[] audioData = audioFile.getBytes();
            
            // 使用新的语音识别服务
            String recognitionResult = voiceRecognitionService.recognizeAudio(audioData);
            
            if (StringUtils.isBlank(recognitionResult)) {
                throw new RuntimeException("语音识别结果为空");
            }
            
            log.info("语音识别成功，识别结果: {}", recognitionResult);
            return recognitionResult;
            
        } catch (Exception e) {
            log.error("语音识别失败", e);
            throw new RuntimeException("语音识别失败: " + e.getMessage());
        }
    }

    /**
     * 通过URL进行语音识别（这里可以先下载音频文件再识别）
     */
    private String recognizeAudioFromUrl(String audioUrl) {
        // TODO: 实现从URL下载音频并识别的逻辑
        throw new RuntimeException("暂不支持通过URL进行语音识别");
    }

    /**
     * 保存用户消息
     */
    private void saveUserMessage(Long sessionId, Long userId, Long digitalHumanId, String content, String messageType) {
        DigitalHumanMessageBo messageBo = new DigitalHumanMessageBo();
        messageBo.setSessionId(sessionId);
        messageBo.setUserId(userId);
        messageBo.setDigitalHumanId(digitalHumanId);
        messageBo.setContent(content);
        messageBo.setMessageType(messageType);
        messageBo.setRole("user");
        
        digitalHumanMessageService.insertByBo(messageBo);
    }

    /**
     * 构建聊天请求
     */
    private ChatRequest buildChatRequest(DigitalHumanVo digitalHuman, DigitalHumanSessionVo session, String userMessage, Long userId,String model) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUserId(userId);
        chatRequest.setSessionId(session.getId());
        chatRequest.setSysPrompt(digitalHuman.getSystemPrompt());
        List<DigitalHumanKnowledgeVo> knowledgeList = digitalHuman.getKnowledgeList();
        List<Long> knowledgeIds = new ArrayList<>();
        if (knowledgeList != null && !knowledgeList.isEmpty()) {
            for (DigitalHumanKnowledgeVo knowledge : knowledgeList) {
                knowledgeIds.add(knowledge.getKnowledgeId());
            }
        }
        chatRequest.setKids(knowledgeIds);
        
        // 构建消息历史
        List<Message> messages = buildMessageHistory(session.getId(), userMessage);
        chatRequest.setMessages(messages);
        
        // 设置模型 暂时写死如果没选择
        if (StringUtils.isEmpty(model)){
            chatRequest.setModel("deepseek-reasoner");
        }else{
            chatRequest.setModel(model);
        }
        // 设置流式输出
        chatRequest.setStream(true);
        
        return chatRequest;
    }

    /**
     * 构建消息历史
     */
    private List<Message> buildMessageHistory(Long sessionId, String currentMessage) {
        List<Message> messages = new ArrayList<>();
        
        // 获取历史消息（限制最近20条）
        List<DigitalHumanMessageVo> history =
            digitalHumanMessageService.queryMessageHistory(sessionId, 20);
        
        // 转换为聊天消息格式
        for (DigitalHumanMessageVo msg : history) {
            Message message = new Message();
            message.setRole(msg.getRole());
            message.setContent(msg.getContent());
            messages.add(message);
        }
        
        // 添加当前用户消息
        Message currentMsg = new Message();
        currentMsg.setRole("user");
        currentMsg.setContent(currentMessage);
        messages.add(currentMsg);
        
        return messages;
    }

    /**
     * 包装SSE发射器以保存数字人消息和更新会话
     */
    private SseEmitter wrapSseEmitterForDigitalHuman(SseEmitter originalEmitter, DigitalHumanSessionVo session, DigitalHumanVo digitalHuman, String outputType) {
        return new DigitalHumanSseEmitter(originalEmitter, session, digitalHuman, outputType, digitalHumanMessageService, digitalHumanSessionService,textToSpeechService);
    }




}
