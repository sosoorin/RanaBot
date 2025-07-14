package com.sosorin.ranabot.controller;

import com.sosorin.ranabot.ResponseModel;
import com.sosorin.bot.IBot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        boolean sent = bot.send(message);
        return sent ? ResponseModel.SUCCESS() : ResponseModel.FAIL();
    }

}
