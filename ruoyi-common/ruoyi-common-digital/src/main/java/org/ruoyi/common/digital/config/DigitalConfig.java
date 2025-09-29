package org.ruoyi.common.digital.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Digital模块自动配置
 * 仿照OpenAI的配置方式
 *
 * @author leeqi
 * @date 2025-09-28
 */
@AutoConfiguration
@ComponentScan(basePackages = {
    "org.ruoyi.common.digital.config",
    "org.ruoyi.common.digital.xfyun"
})
@Import({
    XfyunWebSocketConfig.class
})
public class DigitalConfig {
}
