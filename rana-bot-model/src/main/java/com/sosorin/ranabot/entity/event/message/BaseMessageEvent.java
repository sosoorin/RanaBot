package com.sosorin.ranabot.entity.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息事件基础类
 *
 * @author rana-bot
 * @since 2025/6/26
 */
@Data
public abstract class BaseMessageEvent implements EventBody {
    /**
     * 事件发生时间戳
     */
    @JSONField(name = "time", format = "unixtime")
    private LocalDateTime time;

    /**
     * 事件类型 (message 或 message_sent)
     */
    @JSONField(name = "post_type")
    private String postType;

    /**
     * 收到事件的机器人QQ号
     */
    @JSONField(name = "self_id")
    private Long selfId;

    /**
     * 消息类型 (group 或 private)
     */
    @JSONField(name = "message_type")
    private String messageType;

    /**
     * 消息子类型
     */
    @JSONField(name = "sub_type")
    private String subType;

    /**
     * 消息ID
     */
    @JSONField(name = "message_id")
    private Long messageId;

    /**
     * 发送者QQ号
     */
    @JSONField(name = "user_id")
    private Long userId;

    /**
     * 消息段数组
     */
    @JSONField(name = "message")
    private List<Message> message;

    /**
     * 原始消息内容
     */
    @JSONField(name = "raw_message")
    private String rawMessage;

    /**
     * 字体
     */
    @JSONField(name = "font")
    private Integer font;

    @Override
    public PostType getPostType() {
        return PostType.MESSAGE;
    }
} 