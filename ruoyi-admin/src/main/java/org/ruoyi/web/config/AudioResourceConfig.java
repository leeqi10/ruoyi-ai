package org.ruoyi.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 音频资源访问配置
 * 用于访问TTS生成的音频文件
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Configuration
public class AudioResourceConfig implements WebMvcConfigurer {

    @Value("${ruoyi.profile:/tmp/ruoyi-uploads}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置音频资源访问路径
        registry.addResourceHandler("/audio/**")
                .addResourceLocations("file:" + uploadPath + "/audio/");
        
        // 可以添加其他静态资源路径
        registry.addResourceHandler("/voice/**")
                .addResourceLocations("file:" + uploadPath + "/voice/");
    }
}
