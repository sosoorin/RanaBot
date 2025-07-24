package com.sosorin.ranabot.entity.send;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息发送响应
 * 表示消息发送后的响应数据
 *
 * @author rana-bot
 * @since 2025/6/28 10:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendResponse {
    /**
     * 消息ID
     */
    @JSONField(name = "message_id")
    private Long messageId;
} 