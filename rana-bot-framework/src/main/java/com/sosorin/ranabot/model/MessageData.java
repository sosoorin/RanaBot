package com.sosorin.ranabot.model;

import com.sosorin.ranabot.enums.MessageDataType;

/**
 * 消息数据接口
 */
public interface MessageData {
    /**
     * 获取消息类型
     *
     * @return 消息类型
     */
    MessageDataType getType();
} 