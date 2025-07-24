package com.sosorin.ranabot.controller;

import com.sosorin.ranabot.entity.bot.GroupInfo;
import com.sosorin.ranabot.entity.send.ActionResponse;
import com.sosorin.ranabot.http.ResponseModel;
import com.sosorin.ranabot.entity.bot.IBot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author rana-bot
 * @since 2025/6/26  15:21
 */
@RestController
@RequestMapping("/bot/message")
@Tag(name = "WebSocket消息处理接口")
public class WebSocketMessageController {

    @Autowired
    private IBot bot;

    @PostMapping("/send/raw")
    @Operation(summary = "发送原始消息")
    public ResponseModel<?> send(@RequestBody String message) {
        boolean sent = bot.sendRawMessageStr(message);
        return sent ? ResponseModel.SUCCESS() : ResponseModel.FAIL();
    }

    @GetMapping("/groupList")
    @Operation(summary = "获取群列表")
    public ResponseModel<?> getGroupList() {
        CompletableFuture<ActionResponse<List<GroupInfo>>> future = bot.getGroupList();
        ActionResponse<List<GroupInfo>> response = future.join();
        return response.getStatus().equals(ActionResponse.OK_CODE) ? ResponseModel.SUCCESS(response.getData()) : ResponseModel.FAIL(response.getMessage());
    }

}
