package com.sosorin.ranabot.entity.message;


import com.sosorin.ranabot.enums.MessageDataType;

/**
 * 消息工厂类
 */
public class MessageFactory {

    /**
     * 创建文本消息
     *
     * @param text 文本内容
     * @return 消息对象
     */
    public static Message createTextMessage(String text) {
        TextMessageData data = new TextMessageData();
        data.setText(text);
        return new Message(MessageDataType.TEXT.getType(), data);
    }

    /**
     * 创建表情消息
     *
     * @param id 表情ID
     * @return 消息对象
     */
    public static Message createFaceMessage(String id) {
        FaceMessageData data = new FaceMessageData();
        data.setId(id);
        return new Message(MessageDataType.FACE.getType(), data);
    }

    /**
     * 创建图片消息
     *
     * @param file 图片文件/URL
     * @return 消息对象
     */
    public static Message createImageMessage(String file) {
        ImageMessageData data = new ImageMessageData();
        data.setFile(file);
        return new Message(MessageDataType.IMAGE.getType(), data);
    }

    /**
     * 创建At消息
     *
     * @param qq QQ号或"all"
     * @return 消息对象
     */
    public static Message createAtMessage(String qq) {
        AtMessageData data = new AtMessageData();
        data.setQq(qq);
        return new Message(MessageDataType.AT.getType(), data);
    }

    /**
     * 创建回复消息
     *
     * @param id 消息ID
     * @return 消息对象
     */
    public static Message createReplyMessage(String id) {
        ReplyMessageData data = new ReplyMessageData();
        data.setId(id);
        return new Message(MessageDataType.REPLY.getType(), data);
    }
} 