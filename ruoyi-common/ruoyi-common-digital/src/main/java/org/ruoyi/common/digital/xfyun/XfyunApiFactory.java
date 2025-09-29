package org.ruoyi.common.digital.xfyun;

import org.ruoyi.common.digital.xfyun.entity.XfyunConfig;
import org.ruoyi.common.digital.xfyun.impl.XfyunApiImpl;

/**
 * 科大讯飞API工厂类
 * 仿照OpenAI的工厂模式创建API实例
 *
 * @author leeqi
 * @date 2025-09-28
 */
public class XfyunApiFactory {

    /**
     * 创建XfyunApi实例
     *
     * @param config 科大讯飞配置
     * @return XfyunApi实例
     */
    public static XfyunApi create(XfyunConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("XfyunConfig cannot be null");
        }
        
        if (config.getAppId() == null || config.getAppId().trim().isEmpty()) {
            throw new IllegalArgumentException("AppId cannot be null or empty");
        }
        
        if (config.getApiKey() == null || config.getApiKey().trim().isEmpty()) {
            throw new IllegalArgumentException("ApiKey cannot be null or empty");
        }
        
        return new XfyunApiImpl(config);
    }

    /**
     * 创建XfyunApi实例（使用默认配置）
     *
     * @param appId 应用ID
     * @param apiKey API密钥
     * @return XfyunApi实例
     */
    public static XfyunApi create(String appId, String apiKey) {
        XfyunConfig config = new XfyunConfig();
        config.setAppId(appId);
        config.setApiKey(apiKey);
        return create(config);
    }

    /**
     * 创建XfyunApi实例（使用默认配置和自定义WebSocket URL）
     *
     * @param appId 应用ID
     * @param apiKey API密钥
     * @param wsUrl WebSocket连接URL
     * @return XfyunApi实例
     */
    public static XfyunApi create(String appId, String apiKey, String wsUrl) {
        XfyunConfig config = new XfyunConfig();
        config.setAppId(appId);
        config.setApiKey(apiKey);
        config.setWsUrl(wsUrl);
        return create(config);
    }
}
