package com.sosorin.ranabot;

import com.alibaba.fastjson2.JSONException;
import com.sosorin.ranabot.model.EventBody;
import com.sosorin.ranabot.util.EventParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class RanaBotApplicationTests {


    @Test
    public void testJson() {
        try {
            String text = "{\"self_id\":222222,\"user_id\":222222,\"time\":1645539742,\"message_id\":222333,\"message_seq\":222222,\"real_id\":222,\"real_seq\":\"222\",\"message_type\":\"group\",\"sender\":{\"user_id\":222222222,\"nickname\":\"青木陽菜\",\"card\":\"要楽奈\",\"role\":\"member\"},\"raw_message\":\"抹茶芭菲\",\"font\":14,\"sub_type\":\"normal\",\"message\":[{\"type\":\"text\",\"data\":{\"text\":\"抹茶芭菲\"}}],\"message_format\":\"array\",\"post_type\":\"message\",\"group_id\":22222}";

            log.info("Raw Text: {}", text);
            // 使用工具类解析事件
            EventBody eventBody = EventParseUtil.parseEvent(text);
            log.info("Event Type: {}", eventBody.getPostType());

            log.info("Event Body: {}", eventBody);
        } catch (JSONException e) {
            log.error("JSON解析错误: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("发生未知错误: {}", e.getMessage(), e);
        }
    }
}
