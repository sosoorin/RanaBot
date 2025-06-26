package com.sosorin.ranabot.entity.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 请求事件实体类
 *
 * @author rana-bot
 * @since 2025/6/26  18:31
 */
@Data
public class RequestEventBody implements EventBody {
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
     * 请求类型
     * 可能值: friend(好友请求), group(群请求)
     */
    @JSONField(name = "request_type")
    private String requestType;

    /**
     * 请求子类型，仅在群请求时有效
     * 可能值: add(加群申请), invite(邀请入群)
     */
    @JSONField(name = "sub_type")
    private String subType;

    /**
     * 发送请求的QQ号
     */
    @JSONField(name = "user_id")
    private Long userId;

    /**
     * 群号，仅在群请求时有效
     */
    @JSONField(name = "group_id")
    private Long groupId;

    /**
     * 验证信息
     */
    @JSONField(name = "comment")
    private String comment;

    /**
     * 请求flag，在调用处理请求的API时需要传入
     */
    @JSONField(name = "flag")
    private String flag;

    @Override
    public PostType getPostType() {
        return PostType.REQUEST;
    }
} 