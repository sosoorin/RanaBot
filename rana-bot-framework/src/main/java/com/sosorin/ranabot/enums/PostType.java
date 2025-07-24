package com.sosorin.ranabot.enums;

/**
 * 事件类型枚举
 * 包含OneBot 12和NapCat支持的所有事件类型
 * 
 * @author rana-bot
 * @since 2025/6/26  18:23
 */
public enum PostType {
    META_EVENT("meta_event"),
    MESSAGE("message"),
    MESSAGE_SENT("message_sent"),
    REQUEST("request"),
    NOTICE("notice"),
    GUILD("guild"); // NapCat扩展的频道事件类型

    private final String type;


    PostType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static PostType fromString(String type) {
        for (PostType dataType : PostType.values()) {
            if (dataType.type.equalsIgnoreCase(type)) {
                return dataType;
            }
        }
        return null;
    }
}
