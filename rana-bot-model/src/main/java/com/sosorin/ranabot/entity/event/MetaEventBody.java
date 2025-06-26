package com.sosorin.ranabot.entity.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 元事件实体类
 *
 * @author rana-bot
 * @since 2025/6/26  18:31
 */
@Data
public class MetaEventBody implements EventBody {
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
     * 元事件类型
     * 可能值: lifecycle, heartbeat
     */
    @JSONField(name = "meta_event_type")
    private String metaEventType;

    /**
     * 元事件子类型，仅在lifecycle类型下有效
     * 可能值: enable, disable, connect
     */
    @JSONField(name = "sub_type")
    private String subType;

    /**
     * 心跳状态，仅在heartbeat类型下有效
     */
    @JSONField(name = "status")
    private Object status;

    /**
     * 距离上一次心跳包的时间(毫秒)，仅在heartbeat类型下有效
     */
    @JSONField(name = "interval")
    private Long interval;

    @Override
    public PostType getPostType() {
        return PostType.META_EVENT;
    }
}
