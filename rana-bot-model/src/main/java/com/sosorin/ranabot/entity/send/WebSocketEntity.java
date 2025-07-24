package com.sosorin.ranabot.entity.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * WebSocket请求实体类
 * 符合OneBot 12 和 NapCat API规范的通信实体
 * 
 * @author rana-bot
 * @since 2025/6/26  21:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketEntity {
    /**
     * 要调用的动作名称，如 send_message, get_friend_list 等
     */
    private String action;
    
    /**
     * 动作的参数，根据不同的动作有不同的参数
     */
    private Map<String, Object> params;
    
    /**
     * 回声字段，用于标识请求，会在响应中原样返回
     */
    private String echo;
}
