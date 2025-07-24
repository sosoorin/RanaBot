package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 表情包消息数据
 * NapCat扩展的消息类型，用于发送表情包
 *
 * @author rana-bot
 */
@Data
public class StickerMessageData implements IMessageData {
    /**
     * 表情包ID
     */
    @JSONField(name = "id")
    private String id;
    
    /**
     * 表情包URL
     */
    @JSONField(name = "url")
    private String url;

    @Override
    public MessageDataType getType() {
        return MessageDataType.STICKER;
    }
} 