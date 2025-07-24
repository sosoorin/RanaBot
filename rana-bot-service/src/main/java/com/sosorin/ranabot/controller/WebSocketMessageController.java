package com.sosorin.ranabot.controller;

import com.sosorin.ranabot.entity.bot.GroupInfo;
import com.sosorin.ranabot.entity.bot.IBot;
import com.sosorin.ranabot.exception.WebSocketRequestException;
import com.sosorin.ranabot.http.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        try {
            Object result = bot.sendRawMessageStr(message);
            return ResponseModel.SUCCESS(result);
        } catch (WebSocketRequestException e) {
            return ResponseModel.FAIL(e.getMessage());
        }
    }

    @GetMapping("/groupList")
    @Operation(summary = "获取群列表")
    public ResponseModel<?> getGroupList() {
        try {
            List<GroupInfo> groupList = bot.getGroupList();
            return ResponseModel.SUCCESS(groupList);
        } catch (WebSocketRequestException e) {
            return ResponseModel.FAIL(e.getMessage());
        }
    }

}
