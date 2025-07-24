package com.sosorin.ranabot.entity.bot;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QQ群信息实体类
 *
 * @author rana-bot
 * @since 2025/6/27 10:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfo {
    /**
     * 群号
     */
    @JSONField(name = "group_id")
    private Long groupId;
    
    /**
     * 群名称
     */
    @JSONField(name = "group_name")
    private String groupName;
    
    /**
     * 群备注
     */
    @JSONField(name = "group_remark")
    private String groupRemark;
    
    /**
     * 成员数量
     */
    @JSONField(name = "member_count")
    private Integer memberCount;
    
    /**
     * 最大成员数量
     */
    @JSONField(name = "max_member_count")
    private Integer maxMemberCount;
    
    /**
     * 全员禁言状态
     */
    @JSONField(name = "group_all_shut")
    private Integer groupAllShut;
} 