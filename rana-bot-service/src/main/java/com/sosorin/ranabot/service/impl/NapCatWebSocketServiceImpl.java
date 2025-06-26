package com.sosorin.ranabot.service.impl;

import com.sosorin.ranabot.service.IWebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.WebSocket;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author rana-bot
 * @since 2025/6/26  14:58
 */
@Service
@Slf4j
public class NapCatWebSocketServiceImpl implements IWebSocketService {

    @Resource(name = "napCatWebSocketClient")
    private WebSocket napCatWebSocketClient;

    @Override
    public WebSocket getWebSocketClient() {
        return napCatWebSocketClient;
    }

    @Override
    public boolean send(String message) {
        log.info("发送消息：{}", message);
        //随机延迟0.5 ~ 2s
        long delay = ThreadLocalRandom.current().nextLong(500, 2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return napCatWebSocketClient.send(message);
    }

    @Override
    public boolean close() {
        return napCatWebSocketClient.close(1000, "");
    }


}
