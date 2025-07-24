package com.sosorin.ranabot.entity.send;

import com.sosorin.ranabot.entity.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 消息发送请求实体
 * 用于构建发送消息的请求参数，符合OneBot 12和NapCat API规范
 *
 * @author rana-bot
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSendRequest {
    /**
     * 消息类型，支持private、group
     */
    private String messageType;
    
    /**
     * 目标ID，群聊为群号，私聊为用户ID
     */
    private Long id;
    
    /**
     * 消息内容，消息段列表
     */
    private List<Message> message;
    
    /**
     * 回复的消息ID
     */
    private Long replyMessageId;
    
    /**
     * 私聊消息专用，临时会话来源群号
     */
    private Long tempGroupId;
    
    /**
     * 消息自动分片上限，仅在分片模式下有效
     */
    private Integer autoEscape;
} 