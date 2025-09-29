# 数字人聊天接口文档

## 概述

本文档描述数字人聊天系统的两个核心接口，支持文本和语音输入输出，集成科大讯飞语音处理技术。

## 接口列表

### 1. 数字人聊天接口

**接口地址：** `POST /digital/chat/send`

**功能描述：** 支持文本和语音输入的数字人聊天接口，可配置文本或语音输出。

#### 请求参数

```json
{
  "sessionId": 123456,          // 可选，会话ID，如不传则创建新会话
  "digitalHumanId": 1,          // 必填，数字人ID
  "content": "你好",             // 文本消息时必填
  "messageType": "1",           // 必填，消息类型：1-文本 2-语音 3-图片
  "sessionType": "1",           // 会话类型：1-文字聊天 2-语音通话，默认1
  "outputType": "1",            // 输出类型：1-纯文本 2-文本+语音，默认1
  "audioFile": null,            // 语音文件（语音消息时需要）
  "audioUrl": "",               // 语音文件URL（可选）
  "model": "gpt-3.5-turbo",     // AI模型名称（可选）
  "useKnowledge": true,         // 是否使用知识库增强，默认true
  "stream": true                // 是否流式输出，默认true
}
```

#### 响应格式

**流式响应（SSE）：** 返回`text/event-stream`格式的数据流

```
data: {"content": "你好！", "type": "text"}
data: {"content": "我是数字人小助手", "type": "text"} 
data: {"audioUrl": "/digital-human/audio/tts_xxx.mp3", "type": "audio"}
data: [DONE]
```

#### 使用示例

**文本聊天：**
```javascript
fetch('/digital/chat/send', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify({
    digitalHumanId: 1,
    content: "你好",
    messageType: "1",
    outputType: "1"
  })
})
```

**语音输入文本输出：**
```javascript
const formData = new FormData();
formData.append('digitalHumanId', '1');
formData.append('messageType', '2');
formData.append('outputType', '1');
formData.append('audioFile', audioFile);

fetch('/digital/chat/voice-upload', {
  method: 'POST',
  body: formData
})
```

### 2. 数字人实时语音通话接口

**接口地址：** `POST /digital/chat/voice`

**功能描述：** 实时语音通话，语音输入语音输出。

**请求格式：** `multipart/form-data`

**请求参数：**

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sessionId | Long | 否 | 会话ID，如果不传则创建新会话 |
| digitalHumanId | Long | 是 | 数字人ID |
| content | String | 否 | 消息内容（文本消息时必填） |
| messageType | String | 是 | 消息类型：1-文本 2-语音 |
| audioFile | MultipartFile | 否 | 语音文件（语音消息时需要） |
| model | String | 否 | AI模型名称 |
| outputType | String | 否 | 输出类型：1-文本 2-文本+语音，默认2 |

#### 请求参数

```json
{
  "digitalHumanId": 1,          // 必填，数字人ID
  "audioFile": null,            // 语音文件
  "audioUrl": "",               // 语音文件URL
  "model": "gpt-3.5-turbo"      // AI模型名称（可选）
}
```

#### 响应格式

**流式响应（SSE）：** 返回包含语音的数据流

```
data: {"content": "收到您的语音消息", "type": "text"}
data: {"audioUrl": "/digital-human/audio/tts_xxx.mp3", "duration": 3, "type": "audio"}
data: [DONE]
```

### 3. 语音文件上传聊天接口

**接口地址：** `POST /digital/chat/voice-upload`

**功能描述：** 上传语音文件进行聊天。

#### 请求参数（Form Data）

- `digitalHumanId`: 数字人ID（必填）
- `audioFile`: 语音文件（必填）
- `outputType`: 输出类型，1-纯文本 2-文本+语音（可选，默认1）

## 技术特性

### 语音处理

1. **语音识别**
   - 使用科大讯飞实时语音转写API
   - 支持中文和英文识别
   - 支持多种音频格式：mp3, wav, m4a等

2. **语音合成**
   - 使用科大讯飞文字转语音API
   - 支持多种音色选择
   - 支持语速、音量、音调调节

### 会话管理

1. **自动会话创建**
   - 如果不传sessionId，系统自动创建新会话
   - 会话标题自动生成（取第一条消息的前30个字符）

2. **会话状态管理**
   - 实时更新会话的最后消息和消息数量
   - 支持会话类型区分（文字聊天/语音通话）

### 知识库增强

- 支持结合数字人关联的知识库
- 提供上下文感知的智能回复
- 保持对话连贯性

## 错误处理

### 常见错误码

- `400 Bad Request`: 请求参数错误
- `404 Not Found`: 数字人不存在
- `500 Internal Server Error`: 服务器内部错误

### 错误响应格式

```json
{
  "code": 400,
  "message": "数字人ID不能为空",
  "success": false
}
```

## 配置说明

### 科大讯飞配置

在`application.yml`中配置科大讯飞API：

```yaml
xfyun:
  enabled: true
  app-id: your_app_id
  api-key: your_api_key
  audio:
    format: raw
    sample-rate: 16000
  language:
    default-language: zh_cn
```

### 文件上传配置

```yaml
ruoyi:
  profile: /data/ruoyi-uploads  # 音频文件存储路径
```

## 使用注意事项

1. **音频格式要求**
   - 支持的格式：mp3, wav, m4a, webm
   - 文件大小限制：25MB
   - 推荐采样率：16kHz

2. **性能优化**
   - 使用流式输出减少延迟
   - 语音文件上传支持多种格式
   - 自动音频文件清理机制

3. **安全考虑**
   - 所有接口需要用户认证
   - 音频文件存储采用UUID命名
   - 支持租户隔离

## 前端集成示例

### JavaScript EventSource

```javascript
const eventSource = new EventSource('/digital/chat/send', {
  method: 'POST',
  headers: {'Content-Type': 'application/json'},
  body: JSON.stringify(chatRequest)
});

eventSource.onmessage = function(event) {
  if (event.data === '[DONE]') {
    eventSource.close();
    return;
  }
  
  const data = JSON.parse(event.data);
  if (data.type === 'text') {
    displayText(data.content);
  } else if (data.type === 'audio') {
    playAudio(data.audioUrl);
  }
};
```

### Vue.js 组件

```vue
<template>
  <div class="digital-human-chat">
    <div class="messages" v-for="msg in messages" :key="msg.id">
      <div :class="msg.role">{{ msg.content }}</div>
      <audio v-if="msg.audioUrl" :src="msg.audioUrl" controls></audio>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      messages: [],
      eventSource: null
    }
  },
  methods: {
    sendMessage(content, type = '1') {
      // 发送消息逻辑
    }
  }
}
</script>
```

## 更新日志

- **2025-09-28**: 初版发布，支持基础文本和语音聊天功能
