package org.ruoyi.common.digital.config;

import lombok.extern.slf4j.Slf4j;
import org.ruoyi.common.digital.config.properties.XfyunProperties;
import org.ruoyi.common.digital.xfyun.XfyunApi;
import org.ruoyi.common.digital.xfyun.XfyunApiFactory;
import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * 科大讯飞WebSocket配置
 * 仿照OpenAI的配置方式
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "xfyun.enabled", havingValue = "true")
@EnableConfigurationProperties(XfyunProperties.class)
@EnableWebSocket
public class XfyunWebSocketConfig {

    /**
     * 创建科大讯飞API实例
     * 仿照OpenAI的Bean配置方式
     *
     * @param xfyunProperties 配置属性
     * @return XfyunApi实例
     */
    @Bean
    public XfyunApi xfyunApi(XfyunProperties xfyunProperties) {
        log.info("正在初始化科大讯飞API客户端...");
        
        // 验证必要的配置参数
        if (xfyunProperties.getAppId() == null || xfyunProperties.getAppId().trim().isEmpty()) {
            throw new IllegalArgumentException("科大讯飞AppId不能为空，请检查配置: xfyun.app-id");
        }
        
        if (xfyunProperties.getApiKey() == null || xfyunProperties.getApiKey().trim().isEmpty()) {
            throw new IllegalArgumentException("科大讯飞ApiKey不能为空，请检查配置: xfyun.api-key");
        }
        
        // 构建配置对象
        XfyunConfig config = buildXfyunConfig(xfyunProperties);
        
        // 创建API实例
        XfyunApi api = XfyunApiFactory.create(config);
        
        log.info("科大讯飞API客户端初始化完成，AppId: {}, WsUrl: {}", 
                xfyunProperties.getAppId(), xfyunProperties.getWsUrl());
        
        return api;
    }

    /**
     * 创建科大讯飞配置对象
     *
     * @param properties 配置属性
     * @return XfyunConfig配置对象
     */
    private XfyunConfig buildXfyunConfig(XfyunProperties properties) {
        XfyunConfig config = new XfyunConfig();
        
        // 基本配置
        config.setAppId(properties.getAppId());
        config.setApiKey(properties.getApiKey());
        config.setWsUrl(properties.getWsUrl());
        
        // 超时配置
        config.setConnectTimeout(properties.getConnectTimeout());
        config.setReadTimeout(properties.getReadTimeout());
        config.setRetryCount(properties.getRetryCount());
        
        // 音频配置
        if (properties.getAudio() != null) {
            config.setAudioFormat(properties.getAudio().getFormat());
            config.setSampleRate(properties.getAudio().getSampleRate());
            config.setDataType(properties.getAudio().getDataType());
        }
        
        // 语言配置
        if (properties.getLanguage() != null) {
            config.setLanguage(properties.getLanguage().getDefaultLanguage());
            config.setDomain(properties.getLanguage().getDefaultDomain());
            config.setPunctuation(properties.getLanguage().getPunctuation());
            config.setNumberConvert(properties.getLanguage().getNumberConvert());
        }
        
        return config;
    }
}
