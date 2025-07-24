package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 按钮消息数据
 * NapCat扩展的消息类型
 *
 * @author rana-bot
 */
@Data
public class ButtonMessageData implements IMessageData {
    /**
     * 按钮ID
     */
    @JSONField(name = "id")
    private String id;
    
    /**
     * 按钮文本
     */
    @JSONField(name = "text")
    private String text;
    
    /**
     * 按钮链接，点击后会跳转
     */
    @JSONField(name = "link")
    private String link;
    
    /**
     * 按钮样式，可选值：primary, secondary, danger等
     */
    @JSONField(name = "style")
    private String style;

    @Override
    public MessageDataType getType() {
        return MessageDataType.BUTTON;
    }
} 