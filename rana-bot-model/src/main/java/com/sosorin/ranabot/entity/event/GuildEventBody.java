package com.sosorin.ranabot.entity.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 频道事件实体类
 * 用于处理NapCat中的频道相关事件
 *
 * @author rana-bot
 */
@Data
public class GuildEventBody implements EventBody {
    /**
     * 事件发生时间戳
     */
    @JSONField(name = "time", format = "unixtime")
    private LocalDateTime time;

    /**
     * 事件类型
     */
    @JSONField(name = "post_type")
    private String postType;

    /**
     * 收到事件的机器人QQ号
     */
    @JSONField(name = "self_id")
    private Long selfId;

    /**
     * 频道事件类型
     * 可能值: guild_member, guild_message, guild_channel, etc.
     */
    @JSONField(name = "guild_type")
    private String guildType;

    /**
     * 子类型
     */
    @JSONField(name = "sub_type")
    private String subType;

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
     * 用户ID
     */
    @JSONField(name = "user_id")
    private String userId;

    /**
     * 操作者ID
     */
    @JSONField(name = "operator_id")
    private String operatorId;

    /**
     * 消息ID
     */
    @JSONField(name = "message_id")
    private String messageId;

    /**
     * 事件发生的具体内容
     * 结构根据事件类型不同而变化
     */
    @JSONField(name = "content")
    private Object content;

    @Override
    public PostType getPostType() {
        // 使用新增的GUILD事件类型
        return PostType.GUILD;
    }
} 