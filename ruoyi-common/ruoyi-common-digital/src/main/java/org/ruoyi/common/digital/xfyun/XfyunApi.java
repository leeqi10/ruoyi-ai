package org.ruoyi.common.digital.xfyun;

import io.reactivex.Single;
import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionRequest;
import org.ruoyi.common.digital.xfyun.entity.XfyunTranscriptionResponse;
import org.ruoyi.common.digital.xfyun.entity.XfyunTtsRequest;
import org.ruoyi.common.digital.xfyun.entity.XfyunTtsResponse;
import org.ruoyi.common.digital.xfyun.websocket.XfyunWebSocketClient;

import java.util.function.Consumer;

/**
 * 讯飞开放平台API接口
 * 仿照OpenAI的对接方式进行语音处理
 *
 * @author leeqi
 * @date 2025-09-28
 */
public interface XfyunApi {

    /**
     * 实时语音转写 - 流式处理
     * 类似于OpenAI的流式聊天完成接口
     *
     * @param request 语音转写请求参数
     * @param responseHandler 响应处理器
     * @param errorHandler 错误处理器
     * @return Single<XfyunWebSocketClient>
     */
    Single<XfyunWebSocketClient> realTimeTranscription(
            XfyunTranscriptionRequest request,
            Consumer<XfyunTranscriptionResponse> responseHandler,
            Consumer<String> errorHandler
    );

    /**
     * 实时语音转写 - 同步处理
     * 类似于OpenAI的同步聊天完成接口
     *
     * @param request 语音转写请求参数
     * @return Single<XfyunTranscriptionResponse>
     */
    Single<XfyunTranscriptionResponse> transcription(XfyunTranscriptionRequest request);

    /**
     * 批量语音转写
     * 类似于OpenAI的批量处理接口
     *
     * @param request 语音转写请求参数
     * @return Single<XfyunTranscriptionResponse>
     */
    Single<XfyunTranscriptionResponse> batchTranscription(XfyunTranscriptionRequest request);

    /**
     * 获取支持的音频格式列表
     * 类似于OpenAI的模型列表接口
     *
     * @return Single<String[]> 支持的音频格式
     */
    Single<String[]> getSupportedAudioFormats();

    /**
     * 获取支持的语言列表
     * 类似于OpenAI的模型列表接口
     *
     * @return Single<String[]> 支持的语言
     */
    Single<String[]> getSupportedLanguages();
    /**
     * 获取支持的领域列表
     * 类似于OpenAI的模型列表接口
     *
     * @return Single<String[]> 支持的领域
     */
    Single<String[]> getSupportedDomains();

    /**
     * 验证配置
     * 类似于OpenAI的API密钥验证
     *
     * @param config 科大讯飞配置
     * @return Single<Boolean> 验证结果
     */
    Single<Boolean> validateConfig(XfyunConfig config);

    /**
     * 测试连接
     * 类似于OpenAI的连接测试
     *
     * @param config 科大讯飞配置
     * @return Single<Boolean> 连接测试结果
     */
    Single<Boolean> testConnection(XfyunConfig config);
}
