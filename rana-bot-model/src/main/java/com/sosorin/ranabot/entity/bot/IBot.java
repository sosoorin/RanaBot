package com.sosorin.ranabot.entity.bot;

import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import okhttp3.WebSocket;

import java.util.List;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
public interface IBot {

    public WebSocket getWebSocketClient();

    /**
     * 直接发送string消息
     *
     * @param message 字符串JSON消息
     * @return 返回响应中的data字段值，可能是任意JSON对象
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    Object sendRawMessageStr(String message) throws WebSocketRequestException;

    /**
     * 发送私聊消息
     *
     * @param userId QQ号
     * @param messages 消息
     * @return 发送成功后返回消息ID，失败时抛出异常
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    Long sendPrivateMessage(String userId, Message... messages) throws WebSocketRequestException;

    /**
     * 发送私聊消息
     *
     * @param userId QQ号
     * @param messages 消息列表
     * @return 发送成功后返回消息ID，失败时抛出异常
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    default Long sendPrivateMessage(String userId, List<Message> messages) throws WebSocketRequestException {
        return sendPrivateMessage(userId, messages.toArray(new Message[]{}));
    }

    /**
     * 发送群消息
     *
     * @param groupId 群号
     * @param messages 消息
     * @return 发送成功后返回消息ID，失败时抛出异常
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    Long sendGroupMessage(String groupId, Message... messages) throws WebSocketRequestException;

    /**
     * 发送群消息
     *
     * @param groupId 群号
     * @param messages 消息列表
     * @return 发送成功后返回消息ID，失败时抛出异常
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    default Long sendGroupMessage(String groupId, List<Message> messages) throws WebSocketRequestException {
        return sendGroupMessage(groupId, messages.toArray(new Message[]{}));
    }

    /**
     * 获取QQ群列表信息
     * 
     * @return QQ群信息列表
     * @throws WebSocketRequestException 当API调用失败时抛出异常
     */
    List<GroupInfo> getGroupList() throws WebSocketRequestException;
    
    boolean close();


}
