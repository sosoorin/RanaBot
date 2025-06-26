package com.sosorin.ranabot.entity.event;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.PostType;
import com.sosorin.ranabot.model.EventBody;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知事件实体类
 *
 * @author rana-bot
 * @since 2025/6/26  18:31
 */
@Data
public class NoticeEventBody implements EventBody {
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
     * 通知类型
     * 可能值: group_upload, group_admin, group_decrease, group_increase, group_ban,
     * friend_add, group_recall, friend_recall, poke, lucky_king, honor, etc.
     */
    @JSONField(name = "notice_type")
    private String noticeType;

    /**
     * 通知子类型
     * 视notice_type不同而不同
     */
    @JSONField(name = "sub_type")
    private String subType;

    /**
     * 用户QQ号
     */
    @JSONField(name = "user_id")
    private Long userId;

    /**
     * 群号，某些通知类型下存在
     */
    @JSONField(name = "group_id")
    private Long groupId;

    /**
     * 操作者QQ号，某些通知类型下存在
     */
    @JSONField(name = "operator_id")
    private Long operatorId;

    /**
     * 目标QQ号，某些通知类型下存在
     */
    @JSONField(name = "target_id")
    private Long targetId;

    /**
     * 消息ID，某些通知类型下存在
     */
    @JSONField(name = "message_id")
    private Long messageId;

    /**
     * 荣誉类型，某些通知类型下存在
     * 可能值: talkative:龙王 performer:群聊之火 emotion:快乐源泉
     */
    @JSONField(name = "honor_type")
    private String honorType;

    /**
     * 标题，某些通知类型下存在
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 新名片，群名片变更通知下存在
     */
    @JSONField(name = "card_new")
    private String cardNew;

    /**
     * 旧名片，群名片变更通知下存在
     */
    @JSONField(name = "card_old")
    private String cardOld;

    /**
     * 禁言时长，群禁言通知下存在
     */
    @JSONField(name = "duration")
    private Long duration;

    /**
     * 文件信息，群文件上传通知下存在
     */
    @JSONField(name = "file")
    private FileInfo file;

    /**
     * 设备信息，某些通知类型下存在
     */
    @JSONField(name = "client")
    private Client client;

    @Override
    public PostType getPostType() {
        return PostType.NOTICE;
    }

    /**
     * 文件信息
     */
    @Data
    public static class FileInfo {
        /**
         * 文件ID
         */
        @JSONField(name = "id")
        private String id;

        /**
         * 文件名
         */
        @JSONField(name = "name")
        private String name;

        /**
         * 文件大小
         */
        @JSONField(name = "size")
        private Long size;

        /**
         * 文件URL
         */
        @JSONField(name = "url")
        private String url;

        /**
         * busid，内部使用
         */
        @JSONField(name = "busid")
        private Long busid;
    }

    /**
     * 设备信息
     */
    @Data
    public static class Client {
        /**
         * 应用ID
         */
        @JSONField(name = "app_id")
        private String appId;

        /**
         * 平台
         */
        @JSONField(name = "platform")
        private String platform;

        /**
         * 设备名称
         */
        @JSONField(name = "device_name")
        private String deviceName;

        /**
         * 设备类型
         */
        @JSONField(name = "device_kind")
        private String deviceKind;
    }
} 