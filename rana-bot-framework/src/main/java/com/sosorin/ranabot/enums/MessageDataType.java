package com.sosorin.ranabot.enums;

/**
 * 消息类型枚举
 */
public enum MessageDataType {
    TEXT("text"),
    FACE("face"),
    IMAGE("image"),
    RECORD("record"),
    VIDEO("video"),
    AT("at"),
    RPS("rps"),
    DICE("dice"),
    SHAKE("shake"),
    POKE("poke"),
    SHARE("share"),
    CONTACT("contact"),
    LOCATION("location"),
    MUSIC("music"),
    REPLY("reply"),
    FORWARD("forward"),
    NODE("node"),
    JSON("json"),
    MFACE("mface"),
    FILE("file"),
    MARKDOWN("markdown"),
    LIGHTAPP("lightapp");

    private final String type;

    MessageDataType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static MessageDataType fromString(String type) {
        for (MessageDataType dataType : MessageDataType.values()) {
            if (dataType.type.equalsIgnoreCase(type)) {
                return dataType;
            }
        }
        return null;
    }
} 