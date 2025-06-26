package com.sosorin.ranabot.entity.event.sender;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 私聊发送者信息
 *
 * @author rana-bot
 * @since 2025/6/26
 */
@Data
public class FriendSender {
    /**
     * 发送者QQ号
     */
    @JSONField(name = "user_id")
    private Long userId;

    /**
     * 昵称
     */
    @JSONField(name = "nickname")
    private String nickname;

    /**
     * 性别
     * 可能值: male, female, unknown
     */
    @JSONField(name = "sex")
    private String sex;

    /**
     * 群临时会话群号(可选)
     */
    @JSONField(name = "group_id")
    private Long groupId;
} 