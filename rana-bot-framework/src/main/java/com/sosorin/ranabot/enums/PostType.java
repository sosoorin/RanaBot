package com.sosorin.ranabot.enums;

/**
 * @author rana-bot
 * @since 2025/6/26  18:23
 */
public enum PostType {
    META_EVENT("meta_event"),
    MESSAGE("message"),
    REQUEST("request"),
    NOTICE("notice");

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
