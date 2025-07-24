package com.sosorin.ranabot.bot;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.sosorin.ranabot.entity.bot.GroupInfo;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.entity.message.Message;
import com.sosorin.ranabot.entity.send.ActionResponse;
import com.sosorin.ranabot.entity.send.WebSocketEntity;
import com.sosorin.ranabot.util.SendEntityUtil;
import com.sosorin.ranabot.websocket.WebSocketResponseHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
@Service
@Slf4j
public class NapCatWebSocketBotImpl implements IBot {

    @Resource(name = "napCatWebSocketClient")
    private WebSocket napCatWebSocketClient;
    
    private final WebSocketResponseHandler responseHandler;
    
    @Autowired
    public NapCatWebSocketBotImpl(WebSocketResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public WebSocket getWebSocketClient() {
        return napCatWebSocketClient;
    }

    @Override
    public boolean sendRawMessageStr(String message) {
        log.info("发送消息：{}", message);
        if (StrUtil.isEmpty(message)) {
            log.warn("消息为空");
            return false;
        }
        /*
        //随机延迟0.5 ~ 2s
        long delay = ThreadLocalRandom.current().nextLong(500, 2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
        return napCatWebSocketClient.send(message);
    }

    /**
     * 发送私聊消息
     *
     * @param userId
     * @param messages
     * @return
     */
    @Override
    public boolean sendPrivateMessage(String userId, Message... messages) {
        return sendRawMessageStr(SendEntityUtil.buildSendPrivateMessageStr(userId, messages));
    }

    /**
     * 发送群消息
     *
     * @param groupId
     * @param messages
     * @return
     */
    @Override
    public boolean sendGroupMessage(String groupId, Message... messages) {
        return sendRawMessageStr(SendEntityUtil.buildSendGroupMessageStr(groupId, messages));
    }
    
    /**
     * 获取QQ群列表
     *
     * @return 群信息列表的Future
     */
    @Override
    public CompletableFuture<ActionResponse<List<GroupInfo>>> getGroupList() {
        // 生成唯一的echo标识，用于匹配请求和响应
        String echo = "get_group_list_" + UUID.randomUUID().toString().substring(0, 8);
        
        // 注册请求
        CompletableFuture<ActionResponse<List<GroupInfo>>> future = responseHandler.registerRequest(echo);
        
        // 构建并发送API请求
        WebSocketEntity entity = WebSocketEntity.builder()
                .action("get_group_list")
                .params(new HashMap<>())
                .echo(echo)
                .build();
        
        // 发送请求
        boolean sent = sendRawMessageStr(JSON.toJSONString(entity));
        
        // 如果发送失败，取消请求并返回失败的Future
        if (!sent) {
            responseHandler.cancelRequest(echo);
            CompletableFuture<ActionResponse<List<GroupInfo>>> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(new RuntimeException("发送WebSocket消息失败"));
            return failedFuture;
        }
        
        // 处理结果转换
        return future.thenApply(response -> {
            // 手动转换泛型数据
            if (response.getStatus().equals(ActionResponse.OK_CODE) && response.getData() != null) {
                List<GroupInfo> groupList = JSON.parseObject(
                        JSON.toJSONString(response.getData()),
                        new TypeReference<List<GroupInfo>>() {}
                );
                
                return ActionResponse.<List<GroupInfo>>builder()
                        .status(response.getStatus())
                        .retcode(response.getRetcode())
                        .message(response.getMessage())
                        .data(groupList)
                        .echo(response.getEcho())
                        .build();
            }
            
            return (ActionResponse<List<GroupInfo>>) response;
        });
    }

    @Override
    public boolean close() {
        return napCatWebSocketClient.close(1000, "");
    }

}
