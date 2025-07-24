package com.sosorin.ranabot.entity.message;

import com.alibaba.fastjson2.annotation.JSONField;
import com.sosorin.ranabot.enums.MessageDataType;
import com.sosorin.ranabot.model.IMessageData;
import lombok.Data;

/**
 * 消息段封装类
 * 符合OneBot 12规范中定义的消息段格式
 *
 * @author rana-bot
 * @since 2025/6/26  16:38
 */
@Data
public class Message {
    /**
     * 消息段类型，如text, image等
     */
    @JSONField(name = "type")
    private String type;

    /**
     * 消息段数据，不同类型的消息段有不同的数据格式
     */
    @JSONField(name = "data")
    private IMessageData data;

    public Message() {
    }

    public Message(String type, IMessageData data) {
        this.type = type;
        this.data = data;
    }

    /**
     * 创建一个消息段
     * 
     * @param type 消息段类型
     * @param data 消息段数据
     * @return 消息段对象
     */
    public static Message create(MessageDataType type, IMessageData data) {
        return new Message(type.getType(), data);
    }
    
    /**
     * 创建一个文本消息段
     * 
     * @param text 文本内容
     * @return 文本消息段
     */
    public static Message text(String text) {
        TextMessageData textData = new TextMessageData();
        textData.setText(text);
        return create(MessageDataType.TEXT, textData);
    }
    
    /**
     * 创建一个图片消息段
     * 
     * @param url 图片URL
     * @return 图片消息段
     */
    public static Message image(String url) {
        ImageMessageData imageData = new ImageMessageData();
        imageData.setUrl(url);
        return create(MessageDataType.IMAGE, imageData);
    }
} 