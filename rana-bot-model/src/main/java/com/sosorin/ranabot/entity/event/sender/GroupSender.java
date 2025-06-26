package com.sosorin.ranabot.entity.event.sender;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 群聊发送者信息
 *
 * @author rana-bot
 * @since 2025/6/26
 */
@Data
public class GroupSender {
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
     * 群名片/备注
     */
    @JSONField(name = "card")
    private String card;

    /**
     * 角色
     * 可能值: owner(群主), admin(管理员), member(成员)
     */
    @JSONField(name = "role")
    private String role;
} 