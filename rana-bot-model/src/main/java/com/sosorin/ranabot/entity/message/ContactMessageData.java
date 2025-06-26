package com.sosorin.ranabot.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.MessageData;
import lombok.Data;

/**
 * 联系人推荐消息数据
 */
@Data
public class ContactMessageData implements MessageData {
    private String type; // "qq" 推荐好友 或 "group" 推荐群聊
    private String id; // QQ号或群号

    @JsonIgnore
    @Override
    public MessageDataType getType() {
        return MessageDataType.CONTACT;
    }
} 