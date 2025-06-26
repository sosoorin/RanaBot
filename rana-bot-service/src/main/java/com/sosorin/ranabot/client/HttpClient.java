package com.sosorin.ranabot.client;

import com.alibaba.fastjson2.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author rana-bot
 * @since 2025/6/26  20:25
 */
@Component
public class HttpClient {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * 发送GET请求
     *
     * @param url     请求地址
     * @param headers 请求头（可为null）
     * @return 响应内容字符串
     * @throws IOException 网络异常
     */
    public String get(String url, Map<String, String> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body() != null ? response.body().string() : null;
        }
    }

    /**
     * 发送POST请求（发送JSON数据）
     *
     * @param url      请求地址
     * @param jsonBody JSON字符串
     * @param headers  请求头（可为null）
     * @return 响应内容字符串
     * @throws IOException 网络异常
     */
    public String post(String url, Object jsonBody, Map<String, String> headers) throws IOException {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String jsonString = JSON.toJSONString(jsonBody);
        RequestBody body = RequestBody.create(jsonString, mediaType);
        Request.Builder builder = new Request.Builder().url(url).post(body);
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
        Request request = builder.build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body() != null ? response.body().string() : null;
        }
    }
}
