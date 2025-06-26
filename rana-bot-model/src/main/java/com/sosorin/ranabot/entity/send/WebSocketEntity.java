package com.sosorin.ranabot.entity.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author rana-bot
 * @since 2025/6/26  21:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketEntity {
    private String action;
    private Map<String, Object> params;
    private String echo;
}
