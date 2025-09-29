# 数字人会话和消息查询API接口文档

## 概述
本文档描述了数字人会话和消息的分页查询接口，包括会话管理和消息查询功能。

## 接口列表

### 1. 数字人会话查询接口

#### 1.1 分页查询会话列表
- **接口地址**: `GET /digital/session/list`
- **功能描述**: 分页查询数字人会话信息列表
- **请求参数**:
  - `pageNum`: 页码（可选，默认1）
  - `pageSize`: 每页大小（可选，默认10）
  - `userId`: 用户ID（可选）
  - `digitalHumanId`: 数字人ID（可选）
  - `sessionType`: 会话类型（可选，1-文字聊天 2-语音通话）
  - `title`: 会话标题（可选，模糊查询）
  - `status`: 状态（可选，0-结束 1-进行中）

- **响应示例**:
```json
{
  "total": 100,
  "rows": [
    {
      "id": 1,
      "userId": 123,
      "digitalHumanId": 456,
      "digitalHumanName": "小助手",
      "digitalHumanAvatar": "avatar.jpg",
      "sessionType": "1",
      "title": "聊天会话",
      "lastMessage": "你好，有什么可以帮助您的吗？",
      "messageCount": 10,
      "duration": 300,
      "status": "1",
      "createTime": "2025-09-26 10:00:00",
      "updateTime": "2025-09-26 10:30:00"
    }
  ],
  "code": 200,
  "msg": "操作成功"
}
```

#### 1.2 查询会话详情
- **接口地址**: `GET /digital/session/detail/{id}`
- **功能描述**: 根据会话ID查询会话详情
- **路径参数**:
  - `id`: 会话ID

#### 1.3 查询所有会话（不分页）
- **接口地址**: `GET /digital/session/all`
- **功能描述**: 查询所有会话信息（不分页）
- **请求参数**: 同分页查询接口

### 2. 数字人消息查询接口

#### 2.1 分页查询消息列表
- **接口地址**: `GET /digital/message/list`
- **功能描述**: 分页查询数字人消息信息列表
- **请求参数**:
  - `pageNum`: 页码（可选，默认1）
  - `pageSize`: 每页大小（可选，默认10）
  - `sessionId`: 会话ID（可选）
  - `userId`: 用户ID（可选）
  - `digitalHumanId`: 数字人ID（可选）
  - `content`: 消息内容（可选，模糊查询）
  - `messageType`: 消息类型（可选，1-文本 2-语音 3-图片）
  - `role`: 角色（可选，user-用户 assistant-助手）
  - `modelName`: 模型名称（可选，模糊查询）

- **响应示例**:
```json
{
  "total": 50,
  "rows": [
    {
      "id": 1,
      "sessionId": 123,
      "userId": 456,
      "digitalHumanId": 789,
      "content": "你好，有什么可以帮助您的吗？",
      "messageType": "1",
      "role": "assistant",
      "audioUrl": null,
      "audioDuration": null,
      "tokens": 20,
      "modelName": "gpt-3.5-turbo",
      "createTime": "2025-09-26 10:00:00"
    }
  ],
  "code": 200,
  "msg": "操作成功"
}
```

#### 2.2 查询消息详情
- **接口地址**: `GET /digital/message/detail/{id}`
- **功能描述**: 根据消息ID查询消息详情
- **路径参数**:
  - `id`: 消息ID

#### 2.3 查询所有消息（不分页）
- **接口地址**: `GET /digital/message/all`
- **功能描述**: 查询所有消息信息（不分页）
- **请求参数**: 同分页查询接口

## 使用示例

### 查询用户的会话列表
```bash
curl -X GET "http://localhost:8080/digital/session/list?userId=123&pageNum=1&pageSize=10"
```

### 查询特定数字人的消息
```bash
curl -X GET "http://localhost:8080/digital/message/list?digitalHumanId=456&pageNum=1&pageSize=20"
```

### 查询特定会话的消息
```bash
curl -X GET "http://localhost:8080/digital/message/list?sessionId=789&pageNum=1&pageSize=50"
```

## 技术实现

### 服务层架构
- **IDigitalHumanSessionService**: 会话服务接口
- **DigitalHumanSessionServiceImpl**: 会话服务实现类
- **IDigitalHumanMessageService**: 消息服务接口
- **DigitalHumanMessageServiceImpl**: 消息服务实现类

### 数据层
- **DigitalHumanSessionMapper**: 会话数据访问层
- **DigitalHumanMessageMapper**: 消息数据访问层

### 实体类
- **DigitalHumanSessionBo**: 会话业务对象
- **DigitalHumanMessageBo**: 消息业务对象
- **DigitalHumanSessionVo**: 会话视图对象
- **DigitalHumanMessageVo**: 消息视图对象

## 注意事项
1. 所有查询接口都支持多条件组合查询
2. 分页查询默认按创建时间倒序排列
3. 消息查询按创建时间正序排列（保持对话顺序）
4. 所有接口都遵循RESTful设计规范
5. 支持模糊查询的字段会自动进行LIKE匹配
