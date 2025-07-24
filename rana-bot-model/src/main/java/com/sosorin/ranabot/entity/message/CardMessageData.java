package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 卡片消息数据
 * NapCat扩展的消息类型，用于发送结构化的卡片消息
 *
 * @author rana-bot
 */
@Data
public class CardMessageData implements IMessageData {
    /**
     * 卡片内容，为JSON字符串或对象
     */
    @JSONField(name = "content")
    private Object content;

    @Override
    public MessageDataType getType() {
        return MessageDataType.CARD;
    }
} 