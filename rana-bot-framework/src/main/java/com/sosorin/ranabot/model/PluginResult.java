package com.sosorin.ranabot.model;

import com.sun.nio.sctp.HandlerResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rana-bot
 * @since 2025/7/15  01:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginResult {

    private static final String DEFAULT_SUCCESS_MESSAGE = "success";
    private static final String DEFAULT_FAIL_MESSAGE = "fail";

    private HandlerResult handlerResult = HandlerResult.CONTINUE;

    private String message;

    private boolean success;


    public static PluginResult SUCCESS() {
        return SUCCESS(DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     * 创建一个成功结果，默认返回继续处理
     *
     * @param message 消息
     * @return 结果对象
     */
    public static PluginResult SUCCESS(String message) {
        return CONTINUE(message);
    }

    /**
     * 创建一个成功结果，返回继续处理
     *
     * @param message 要打印的消息
     * @return PluginResult
     */
    public static PluginResult CONTINUE(String message) {
        return new PluginResult(HandlerResult.CONTINUE, message, true);
    }

    /**
     * 创建一个成功结果，返回继续处理
     *
     * @return PluginResult
     */
    public static PluginResult CONTINUE() {
        return CONTINUE(DEFAULT_SUCCESS_MESSAGE);
    }


    /**
     * 创建一个成功结果，返回结束处理
     *
     * @param message 要打印的消息
     * @return PluginResult
     */
    public static PluginResult RETURN(String message) {
        return new PluginResult(HandlerResult.RETURN, message, true);
    }

    /**
     * 创建一个成功结果，返回结束处理
     *
     * @return
     */
    public static PluginResult RETURN() {
        return RETURN(DEFAULT_SUCCESS_MESSAGE);
    }


    /**
     * 创建一个成功结果，自定义处理结果
     *
     * @param message       要打印的消息
     * @param handlerResult 处理结果
     * @return 结果对象
     */
    public static PluginResult SUCCESS(String message, HandlerResult handlerResult) {
        if (handlerResult == null) {
            handlerResult = HandlerResult.CONTINUE;
        }
        return new PluginResult(handlerResult, message, true);
    }

    /**
     * 创建失败结果对象
     *
     * @param message 错误信息
     * @return 结果对象
     */
    public static PluginResult FAIL(String message) {
        return new PluginResult(HandlerResult.CONTINUE, message, false);
    }

    public static PluginResult FAIL() {
        return FAIL(DEFAULT_FAIL_MESSAGE);
    }


}
