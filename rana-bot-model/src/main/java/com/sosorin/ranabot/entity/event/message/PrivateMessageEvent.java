package com.sosorin.ranabot.entity.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.entity.event.sender.FriendSender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 私聊消息事件
 *
 * @author rana-bot
 * @since 2025/6/26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PrivateMessageEvent extends BaseMessageEvent {
    /**
     * 临时会话目标QQ号（可选）
     */
    @JSONField(name = "target_id")
    private Long targetId;

    /**
     * 临时会话来源（可选）
     */
    @JSONField(name = "temp_source")
    private Integer tempSource;

    /**
     * 发送者信息
     */
    @JSONField(name = "sender")
    private FriendSender sender;

    /**
     * 初始化时设置消息类型为私聊
     */
    public PrivateMessageEvent() {
        setMessageType("private");
    }
} 