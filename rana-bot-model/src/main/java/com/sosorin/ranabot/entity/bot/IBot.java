package com.sosorin.ranabot.entity.bot;

import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.send.ActionResponse;
import okhttp3.WebSocket;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
public interface IBot {

    public WebSocket getWebSocketClient();

    /**
     * 直接发送string消息
     *
     * @param message 字符串
     * @return 发送结果
     */
    boolean sendRawMessageStr(String message);

    /**
     * 发送私聊消息
     *
     * @param userId QQ号
     * @param messages 消息
     * @return 发送结果
     */
    boolean sendPrivateMessage(String userId, Message... messages);

    /**
     * 发送群消息
     *
     * @param groupId 群号
     * @param messages 消息
     * @return 发送结果
     */
    boolean sendGroupMessage(String groupId, Message... messages);

    /**
     * 获取QQ群列表信息
     * 
     * @return 包含群信息列表的CompletableFuture对象
     */
    CompletableFuture<ActionResponse<List<GroupInfo>>> getGroupList();
    
    boolean close();


}
