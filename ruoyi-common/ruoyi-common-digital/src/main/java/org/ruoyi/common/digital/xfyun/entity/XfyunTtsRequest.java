package org.ruoyi.common.digital.xfyun.entity;

import lombok.Data;

/**
 * 科大讯飞文字转语音请求参数
 *
 * @author leeqi
 * @date 2025-09-28
 */
@Data
public class XfyunTtsRequest {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 需要合成的文本
     */
    private String text;

    /**
     * 发音人（音色）
     * 默认: xiaoyan（小燕）
     * 可选: xiaoyu（小宇）、xiaomeng（小梦）、aisjiuxu（爱思久续）等
     */
    private String voiceName = "xiaoyan";

    /**
     * 音频编码格式
     * 可选: raw（未压缩的pcm）、lame（压缩的mp3）
     */
    private String audioEncoding = "lame";

    /**
     * 音频采样率
     * 可选: 8000、16000、24000
     */
    private String sampleRate = "16000";

    /**
     * 语速，范围 [0-100]，默认为50
     */
    private String speed = "50";

    /**
     * 音量，范围 [0-100]，默认为50
     */
    private String volume = "50";

    /**
     * 音调，范围 [0-100]，默认为50
     */
    private String pitch = "50";

    /**
     * 音频格式
     * pcm: 未压缩音频
     * wav: wave格式
     * mp3: mp3格式
     */
    private String audioFormat = "mp3";

}
