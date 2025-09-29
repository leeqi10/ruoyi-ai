package org.ruoyi.common.digital.xfyun.constant;

/**
 * 科大讯飞常量定义
 * 仿照OpenAI的常量定义方式
 *
 * @author leeqi
 * @date 2025-09-28
 */
public class XfyunConst {

    /**
     * 默认WebSocket连接URL
     */
    public static final String DEFAULT_WS_URL = "wss://rtasr.xfyun.cn/v1/ws";

    /**
     * 默认连接超时时间（毫秒）
     */
    public static final Integer DEFAULT_CONNECT_TIMEOUT = 30000;

    /**
     * 默认读取超时时间（毫秒）
     */
    public static final Integer DEFAULT_READ_TIMEOUT = 60000;

    /**
     * 默认写入超时时间（毫秒）
     */
    public static final Integer DEFAULT_WRITE_TIMEOUT = 60000;

    /**
     * 默认重试次数
     */
    public static final Integer DEFAULT_RETRY_COUNT = 3;

    /**
     * 默认重试间隔（毫秒）
     */
    public static final Integer DEFAULT_RETRY_INTERVAL = 1000;

    /**
     * 默认音频分片大小（字节）
     */
    public static final Integer DEFAULT_CHUNK_SIZE = 1280;

    /**
     * 默认分片发送间隔（毫秒）
     */
    public static final Integer DEFAULT_CHUNK_INTERVAL = 40;

    /**
     * 支持的音频格式
     */
    public static class AudioFormat {
        public static final String RAW = "raw";
        public static final String SPEEX = "speex";
        public static final String OPUS = "opus";
        public static final String SPEEX_WB = "speex-wb";
        public static final String OPUS_WB = "opus-wb";
    }

    /**
     * 支持的采样率
     */
    public static class SampleRate {
        public static final String RATE_8000 = "8000";
        public static final String RATE_16000 = "16000";
    }

    /**
     * 支持的数据编码
     */
    public static class DataType {
        public static final String RAW = "raw";
        public static final String SPEEX = "speex";
        public static final String OPUS = "opus";
    }

    /**
     * 支持的语言
     */
    public static class Language {
        public static final String ZH_CN = "zh_cn";
        public static final String EN_US = "en_us";
    }

    /**
     * 支持的领域
     */
    public static class Domain {
        public static final String IAT = "iat";
        public static final String MEDICAL = "medical";
        public static final String FINANCE = "finance";
        public static final String EDUCATION = "education";
        public static final String CAR = "car";
        public static final String SMART_HOME = "smart_home";
    }

    /**
     * 标点符号设置
     */
    public static class Punctuation {
        public static final String ENABLED = "1";
        public static final String DISABLED = "0";
    }

    /**
     * 数字转换设置
     */
    public static class NumberConvert {
        public static final String CHINESE = "zh";
        public static final String ENGLISH = "en";
        public static final String DISABLED = "0";
    }

    /**
     * 错误码
     */
    public static class ErrorCode {
        public static final Integer SUCCESS = 0;
        public static final Integer AUTH_FAILED = 10005;
        public static final Integer PARAM_ERROR = 10006;
        public static final Integer NETWORK_ERROR = 10007;
        public static final Integer SERVICE_ERROR = 10008;
        public static final Integer TIMEOUT = 10009;
        public static final Integer QUOTA_EXCEEDED = 10010;
    }

    /**
     * 错误信息
     */
    public static class ErrorMessage {
        public static final String SUCCESS = "成功";
        public static final String AUTH_FAILED = "认证失败";
        public static final String PARAM_ERROR = "参数错误";
        public static final String NETWORK_ERROR = "网络错误";
        public static final String SERVICE_ERROR = "服务错误";
        public static final String TIMEOUT = "请求超时";
        public static final String QUOTA_EXCEEDED = "配额超限";
    }

    /**
     * 状态码
     */
    public static class Status {
        public static final Integer CONNECTING = 0;
        public static final Integer CONNECTED = 1;
        public static final Integer AUTHENTICATED = 2;
        public static final Integer ERROR = -1;
        public static final Integer CLOSED = -2;
    }

    /**
     * 转写结果类型
     */
    public static class ResultType {
        public static final Integer INTERMEDIATE = 0; // 中间结果
        public static final Integer FINAL = 1;        // 最终结果
    }
}
