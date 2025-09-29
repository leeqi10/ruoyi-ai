-- 数字人相关表结构
-- 变更日期: 2025-09-24
-- 负责人: leeqi
-- 说明: 创建数字人功能相关的数据库表

-- ----------------------------
-- Table structure for digital_human
-- ----------------------------
DROP TABLE IF EXISTS `digital_human`;
CREATE TABLE `digital_human`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '数字人ID',
  `user_id` bigint(20) NOT NULL COMMENT '创建用户ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数字人名称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `personality_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '人格类型(MBTI)',
  `system_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '系统提示词',
  `voice_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '音色ID',
  `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字人信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for digital_human_knowledge
-- ----------------------------
DROP TABLE IF EXISTS `digital_human_knowledge`;
CREATE TABLE `digital_human_knowledge`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `digital_human_id` bigint(20) NOT NULL COMMENT '数字人ID',
  `knowledge_id` bigint(20) NOT NULL COMMENT '知识库ID',
  `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_digital_knowledge`(`digital_human_id`, `knowledge_id`) USING BTREE,
  INDEX `idx_digital_human_id`(`digital_human_id`) USING BTREE,
  INDEX `idx_knowledge_id`(`knowledge_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字人知识库关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for digital_human_voice
-- ----------------------------
DROP TABLE IF EXISTS `digital_human_voice`;
CREATE TABLE `digital_human_voice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '音色ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `voice_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '音色名称',
  `voice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '音色类型(1-预设音色 2-克隆音色)',
  `voice_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '音色代码(预设音色使用)',
  `voice_model_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '音色模型地址(克隆音色使用)',
  `sample_audio_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样本音频地址',
  `voice_config` json NULL COMMENT '音色配置参数',
  `is_default_public` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否默认公开(0-否 1-是)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0-禁用 1-启用 2-训练中)',
  `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_voice_type`(`voice_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字人音色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for digital_human_session
-- ----------------------------
DROP TABLE IF EXISTS `digital_human_session`;
CREATE TABLE `digital_human_session`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `digital_human_id` bigint(20) NOT NULL COMMENT '数字人ID',
  `session_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '会话类型(1-文字聊天 2-语音通话)',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会话标题',
  `last_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '最后一条消息',
  `message_count` int(11) NULL DEFAULT 0 COMMENT '消息数量',
  `duration` int(11) NULL DEFAULT 0 COMMENT '通话时长(秒)',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态(0-结束 1-进行中)',
  `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_digital_human_id`(`digital_human_id`) USING BTREE,
  INDEX `idx_session_type`(`session_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字人会话表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for digital_human_message
-- ----------------------------
DROP TABLE IF EXISTS `digital_human_message`;
CREATE TABLE `digital_human_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `session_id` bigint(20) NOT NULL COMMENT '会话ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `digital_human_id` bigint(20) NOT NULL COMMENT '数字人ID',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `message_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '消息类型(1-文本 2-语音 3-图片)',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色(user-用户 assistant-助手)',
  `audio_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语音文件地址',
  `audio_duration` int(11) NULL DEFAULT 0 COMMENT '语音时长(秒)',
  `tokens` int(11) NULL DEFAULT 0 COMMENT 'Token数量',
  `model_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用的模型名称',
  `create_dept` bigint(20) NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00000' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_session_id`(`session_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_digital_human_id`(`digital_human_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数字人消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 初始化数字人
-- ----------------------------
INSERT INTO `digital_human`
(`user_id`, `name`, `avatar`, `description`, `personality_type`, `system_prompt`, `voice_id`, `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `del_flag`, `tenant_id`)
VALUES
    (1, 'Harry Potter', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '勇敢而充满正义感的年轻巫师。', 'ENFJ', 'You are Harry Potter, brave and loyal.', '1', 103, 1, NOW(), 1, NOW(), '格兰芬多的勇士', '0', '00000'),

    (1, 'Hermione Granger', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '聪明机智，勤奋刻苦，热爱学习。', 'INTJ', 'You are Hermione, logical and knowledgeable.', '2', 103, 1, NOW(), 1, NOW(), '格兰芬多的学霸', '0', '00000'),

    (1, 'Ron Weasley', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '忠诚善良，偶尔笨拙但非常可靠。', 'ISFP', 'You are Ron, loyal and humorous.', '3', 103, 1, NOW(), 1, NOW(), '格兰芬多的好朋友', '0', '00000'),

    (1, 'Albus Dumbledore', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '睿智而仁慈的霍格沃茨校长。', 'INFJ', 'You are Dumbledore, wise and kind.', '4', 103, 1, NOW(), 1, NOW(), '最强大的巫师之一', '0', '00000'),

    (1, 'Severus Snape', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '冷酷外表下隐藏复杂情感的魔药大师。', 'INTP', 'You are Snape, strict but intelligent.', '5', 103, 1, NOW(), 1, NOW(), '双面间谍，深情如海', '0', '00000'),

    (1, 'Lord Voldemort', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '恐怖而渴望永生的黑魔王。', 'ENTJ', 'You are Voldemort, ambitious and ruthless.', '1', 103, 1, NOW(), 1, NOW(), '黑魔王', '0', '00000'),

    (1, 'Draco Malfoy', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '高傲但内心并非真正邪恶。', 'ESTJ', 'You are Draco, proud and conflicted.', '2', 103, 1, NOW(), 1, NOW(), '马尔福家族继承人', '0', '00000'),

    (1, 'Sirius Black', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '桀骜不驯却极富正义感的巫师。', 'ENTP', 'You are Sirius, rebellious but caring.', '1', 103, 1, NOW(), 1, NOW(), '哈利的教父', '0', '00000'),

    (1, 'Rubeus Hagrid', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '半巨人，看似粗犷却心地善良。', 'ISFJ', 'You are Hagrid, kind and warm.', '1', 103, 1, NOW(), 1, NOW(), '神奇生物保护课教师', '0', '00000'),

    (1, 'Luna Lovegood', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '古灵精怪，想象力丰富的拉文克劳学生。', 'INFP', 'You are Luna, dreamy and unique.', '1', 103, 1, NOW(), 1, NOW(), '独特而纯真的灵魂', '0', '00000'),

    (1, 'Neville Longbottom', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '起初胆怯，最终成长为勇敢的巫师。', 'ISFJ', 'You are Neville, humble but brave.', '1', 103, 1, NOW(), 1, NOW(), '草药学高手', '0', '00000'),

    (1, 'Minerva McGonagall', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '严厉但关心学生的变形课教授。', 'ESTJ', 'You are McGonagall, strict but fair.', '1', 103, 1, NOW(), 1, NOW(), '格兰芬多院长', '0', '00000'),

    (1, 'Remus Lupin', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '温和聪慧却受狼人诅咒折磨的教师。', 'INFJ', 'You are Lupin, gentle and wise.', '1', 103, 1, NOW(), 1, NOW(), '黑魔法防御课教授', '0', '00000'),

    (1, 'Bellatrix Lestrange', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '疯狂忠诚于伏地魔的女巫。', 'ESTP', 'You are Bellatrix, fierce and loyal to darkness.', '1', 103, 1, NOW(), 1, NOW(), '食死徒', '0', '00000'),

    (1, 'Ginny Weasley', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '坚强果敢，拥有出色的魁地奇天赋。', 'ENFP', 'You are Ginny, bold and independent.', '1', 103, 1, NOW(), 1, NOW(), '哈利的挚爱', '0', '00000'),

    (1, 'Cho Chang', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '温柔体贴，曾是拉文克劳的魁地奇追球手。', 'ISFP', 'You are Cho, kind and emotional.', '1', 103, 1, NOW(), 1, NOW(), '哈利的初恋', '0', '00000'),

    (1, 'Dobby', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '善良的家养小精灵，勇敢而无私。', 'ESFP', 'You are Dobby, loyal and selfless.', '1', 103, 1, NOW(), 1, NOW(), '为自由而战的小精灵', '0', '00000'),

    (1, 'Lucius Malfoy', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '狡猾而追求权力的巫师。', 'ENTJ', 'You are Lucius, ambitious and cunning.', '1', 103, 1, NOW(), 1, NOW(), '马尔福家族族长', '0', '00000'),

    (1, 'Horace Slughorn', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '喜欢结交人脉的魔药大师。', 'ESFJ', 'You are Slughorn, social and resourceful.', '1', 103, 1, NOW(), 1, NOW(), '霍格沃茨魔药课教授', '0', '00000'),

    (1, 'Nymphadora Tonks', 'https://ruoyi-ai-1254149996.cos.ap-guangzhou.myqcloud.com/2025/05/24/727370b029b648ea968977085da2b20f.jpg', '活泼幽默，擅长变形的傲罗。', 'ENFP', 'You are Tonks, cheerful and brave.', '1', 103, 1, NOW(), 1, NOW(), '凤凰社成员', '0', '00000');
-- ----------------------------
-- 初始化声音
-- ----------------------------
INSERT INTO `digital_human_voice` (
    `user_id`, `voice_name`, `voice_type`, `voice_code`, `voice_model_url`,
    `sample_audio_url`, `voice_config`, `is_default_public`, `status`,
    `create_dept`, `create_by`, `create_time`, `update_by`, `update_time`,
    `remark`, `del_flag`, `tenant_id`
) VALUES
-- 预设音色 1
(1, '预设音色-温柔女声', '1', 'PRESET_001', NULL,
 'https://example.com/audio/preset_001.mp3', '{"pitch":"medium","speed":"normal"}', '1', '1',
 103, 1, NOW(), 1, NOW(),
 '系统预设音色：温柔女声', '0', '00000'),

-- 预设音色 2
(1, '预设音色-沉稳男声', '1', 'PRESET_002', NULL,
 'https://example.com/audio/preset_002.mp3', '{"pitch":"low","speed":"slow"}', '1', '1',
 103, 1, NOW(), 1, NOW(),
 '系统预设音色：沉稳男声', '0', '00000'),

-- 预设音色 3
(1, '预设音色-活力童声', '1', 'PRESET_003', NULL,
 'https://example.com/audio/preset_003.mp3', '{"pitch":"high","speed":"fast"}', '1', '1',
 103, 1, NOW(), 1, NOW(),
 '系统预设音色：活力童声', '0', '00000'),

-- 克隆音色 1
(1, '克隆音色-自定义A', '2', NULL, 'https://example.com/model/clone_a.bin',
 'https://example.com/audio/clone_a.mp3', '{"tone":"custom","clarity":"high"}', '1', '2',
 103, 1, NOW(), 1, NOW(),
 '用户训练的克隆音色 A', '0', '00000'),

-- 克隆音色 2
(1, '克隆音色-自定义B', '2', NULL, 'https://example.com/model/clone_b.bin',
 'https://example.com/audio/clone_b.mp3', '{"tone":"deep","clarity":"medium"}', '0', '2',
 103, 1, NOW(), 1, NOW(),
 '用户训练的克隆音色 B', '0', '00000');


INSERT INTO `digital_human_knowledge` (
    `digital_human_id`, `knowledge_id`,
    `create_dept`, `create_by`, `create_time`,
    `update_by`, `update_time`, `remark`, `del_flag`, `tenant_id`
) VALUES
-- 数字人 1
(1, 1, 103, 1, NOW(), 1, NOW(), '数字人1 关联知识库1', '0', '00000'),
(1, 2, 103, 1, NOW(), 1, NOW(), '数字人1 关联知识库2', '0', '00000'),

-- 数字人 2
(2, 2, 103, 1, NOW(), 1, NOW(), '数字人2 关联知识库2', '0', '00000'),
(2, 3, 103, 1, NOW(), 1, NOW(), '数字人2 关联知识库3', '0', '00000'),

-- 数字人 3
(3, 3, 103, 1, NOW(), 1, NOW(), '数字人3 关联知识库3', '0', '00000'),
(3, 4, 103, 1, NOW(), 1, NOW(), '数字人3 关联知识库4', '0', '00000'),

-- 数字人 4
(4, 4, 103, 1, NOW(), 1, NOW(), '数字人4 关联知识库4', '0', '00000'),
(4, 5, 103, 1, NOW(), 1, NOW(), '数字人4 关联知识库5', '0', '00000'),

-- 数字人 5
(5, 5, 103, 1, NOW(), 1, NOW(), '数字人5 关联知识库5', '0', '00000'),
(5, 6, 103, 1, NOW(), 1, NOW(), '数字人5 关联知识库6', '0', '00000'),

-- 数字人 6
(6, 6, 103, 1, NOW(), 1, NOW(), '数字人6 关联知识库6', '0', '00000'),
(6, 7, 103, 1, NOW(), 1, NOW(), '数字人6 关联知识库7', '0', '00000'),

-- 数字人 7
(7, 7, 103, 1, NOW(), 1, NOW(), '数字人7 关联知识库7', '0', '00000'),
(7, 8, 103, 1, NOW(), 1, NOW(), '数字人7 关联知识库8', '0', '00000'),

-- 数字人 8
(8, 8, 103, 1, NOW(), 1, NOW(), '数字人8 关联知识库8', '0', '00000'),
(8, 9, 103, 1, NOW(), 1, NOW(), '数字人8 关联知识库9', '0', '00000'),

-- 数字人 9
(9, 9, 103, 1, NOW(), 1, NOW(), '数字人9 关联知识库9', '0', '00000'),
(9, 10, 103, 1, NOW(), 1, NOW(), '数字人9 关联知识库10', '0', '00000'),

-- 数字人 10
(10, 10, 103, 1, NOW(), 1, NOW(), '数字人10 关联知识库10', '0', '00000'),
(10, 1, 103, 1, NOW(), 1, NOW(), '数字人10 关联知识库1', '0', '00000');
