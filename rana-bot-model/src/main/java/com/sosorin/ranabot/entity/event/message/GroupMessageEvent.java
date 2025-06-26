package com.sosorin.ranabot.entity.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.entity.event.sender.GroupSender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 群聊消息事件
 *
 * @author rana-bot
 * @since 2025/6/26
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends BaseMessageEvent {
    /**
     * 群号
     */
    @JSONField(name = "group_id")
    private Long groupId;

    /**
     * 发送者信息
     */
    @JSONField(name = "sender")
    private GroupSender sender;

    /**
     * 匿名信息，仅当消息来自匿名用户时有效
     */
    @JSONField(name = "anonymous")
    private Anonymous anonymous;

    /**
     * 初始化时设置消息类型为群聊
     */
    public GroupMessageEvent() {
        setMessageType("group");
    }

    /**
     * 匿名用户信息
     */
    @Data
    public static class Anonymous {
        /**
         * 匿名用户ID
         */
        @JSONField(name = "id")
        private Long id;

        /**
         * 匿名用户名称
         */
        @JSONField(name = "name")
        private String name;

        /**
         * 匿名用户flag
         */
        @JSONField(name = "flag")
        private String flag;
    }
} 