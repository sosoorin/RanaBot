package com.sosorin.ranabot.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.PostType;

import java.time.LocalDateTime;

/**
 * @author rana-bot
 * @since 2025/6/26  18:22
 * WebSocket上报事件实体类接口
 * 所有事件的基础接口，包含时间戳、事件类型和机器人QQ号
 */
public interface EventBody {
    /**
     * 获取事件类型
     *
     * @return 事件类型
     */
    PostType getPostType();

    /**
     * 获取事件发生的时间戳(秒)
     *
     * @return 时间戳
     */
    @JSONField(name = "time")
    LocalDateTime getTime();

    /**
     * 获取收到事件的机器人QQ号
     *
     * @return 机器人QQ号
     */
    @JSONField(name = "self_id")
    Long getSelfId();
}
