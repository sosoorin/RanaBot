package com.sosorin.ranabot.entity.event.sender;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 频道消息发送者信息
 * 用于NapCat中的频道消息事件
 *
 * @author rana-bot
 */
@Data
public class GuildSender {
    /**
     * 用户ID
     */
    @JSONField(name = "user_id")
    private String userId;
    
    /**
     * 用户在频道内的昵称
     */
    @JSONField(name = "nickname")
    private String nickname;
    
    /**
     * 用户在频道内的身份组ID列表
     */
    @JSONField(name = "roles")
    private String[] roles;
    
    /**
     * 用户的QQ号
     */
    @JSONField(name = "tiny_id")
    private String tinyId;
    
    /**
     * 用户头像URL
     */
    @JSONField(name = "avatar")
    private String avatar;
    
    /**
     * 是否为管理员
     */
    @JSONField(name = "is_admin")
    private Boolean isAdmin;
    
    /**
     * 加入时间
     */
    @JSONField(name = "join_time")
    private Long joinTime;
} 