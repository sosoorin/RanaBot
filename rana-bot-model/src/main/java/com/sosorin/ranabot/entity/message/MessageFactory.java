package com.sosorin.ranabot.entity.message;


import com.sosorin.ranabot.enums.MessageDataType;

/**
 * 消息工厂类
 * 包含创建各种消息段的工厂方法
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
    
    /**
     * 创建按钮消息
     *
     * @param id 按钮ID
     * @param text 按钮文本
     * @return 消息对象
     */
    public static Message createButtonMessage(String id, String text) {
        ButtonMessageData data = new ButtonMessageData();
        data.setId(id);
        data.setText(text);
        return new Message(MessageDataType.BUTTON.getType(), data);
    }

    /**
     * 创建卡片消息
     *
     * @param content 卡片内容
     * @return 消息对象
     */
    public static Message createCardMessage(Object content) {
        CardMessageData data = new CardMessageData();
        data.setContent(content);
        return new Message(MessageDataType.CARD.getType(), data);
    }

    /**
     * 创建表情包消息
     *
     * @param id 表情包ID
     * @return 消息对象
     */
    public static Message createStickerMessage(String id) {
        StickerMessageData data = new StickerMessageData();
        data.setId(id);
        return new Message(MessageDataType.STICKER.getType(), data);
    }

    /**
     * 创建语音消息
     *
     * @param url 语音URL
     * @return 消息对象
     */
    public static Message createVoiceMessage(String url) {
        VoiceMessageData data = new VoiceMessageData();
        data.setUrl(url);
        return new Message(MessageDataType.VOICE.getType(), data);
    }

    /**
     * 创建天气消息
     *
     * @param city 城市名称
     * @return 消息对象
     */
    public static Message createWeatherMessage(String city) {
        WeatherMessageData data = new WeatherMessageData();
        data.setCity(city);
        return new Message(MessageDataType.WEATHER.getType(), data);
    }
} 