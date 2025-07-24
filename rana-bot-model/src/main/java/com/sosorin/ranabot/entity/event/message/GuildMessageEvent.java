package com.sosorin.ranabot.entity.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.entity.event.sender.GuildSender;
import com.sosorin.ranabot.entity.message.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 频道消息事件
 * 用于处理NapCat中的频道消息事件
 *
 * @author rana-bot
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GuildMessageEvent extends BaseMessageEvent {
    /**
     * 消息类型，固定为guild
     */
    @JSONField(name = "message_type")
    private final String messageType = "guild";

    /**
     * 频道ID
     */
    @JSONField(name = "guild_id")
    private String guildId;

    /**
     * 子频道ID
     */
    @JSONField(name = "channel_id")
    private String channelId;

    /**
     * 发送者信息
     */
    @JSONField(name = "sender")
    private GuildSender sender;

    /**
     * 消息内容
     */
    @JSONField(name = "message")
    private List<Message> message;

    /**
     * 原始消息内容
     */
    @JSONField(name = "raw_message")
    private String rawMessage;
} 