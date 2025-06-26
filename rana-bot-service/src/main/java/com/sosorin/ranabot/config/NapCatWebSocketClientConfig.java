package com.sosorin.ranabot.config;

import com.sosorin.ranabot.plugin.PluginManager;
import com.sosorin.ranabot.websocket.NapCatWebSocketListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rana-bot
 * @since 2025/6/26  15:09
 */
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "nap-cat.ws")
@Data
public class NapCatWebSocketClientConfig {

    private String url = "ws://localhost:3001";

    private String token = "default_token";

    final OkHttpClient client = new OkHttpClient().newBuilder().build();

    @Autowired
    private PluginManager pluginManager;


    @Bean("napCatWebSocketClient")
    public WebSocket napCatWebSocketClient() {
        Request request = new Request.Builder().url(url).addHeader("Authorization", token).build();
        WebSocket webSocket = client.newWebSocket(request, new NapCatWebSocketListener(pluginManager));
        return webSocket;
    }
}
