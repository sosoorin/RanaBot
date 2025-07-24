package com.sosorin.ranabot.entity.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API调用响应实体
 * 符合OneBot 12和NapCat API规范的响应格式
 *
 * @author rana-bot
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionResponse<T> {
    /**
     * 状态，表示API是否调用成功
     * ok 表示调用成功
     * failed 表示调用失败
     */
    private String status;
    
    /**
     * 错误码，在调用失败时给出
     */
    private Integer retcode;
    
    /**
     * 错误信息，在调用失败时给出
     */
    private String message;
    
    /**
     * 响应数据，根据不同API返回不同的结构
     */
    private T data;
    
    /**
     * 回声，如果请求中有设置echo，响应中会原样返回
     */
    private String echo;
    
    /**
     * 创建一个成功的响应
     * 
     * @param data 响应数据
     * @param echo 回声
     * @return 响应对象
     */
    public static <T> ActionResponse<T> ok(T data, String echo) {
        return ActionResponse.<T>builder()
                .status("ok")
                .data(data)
                .echo(echo)
                .build();
    }
    
    /**
     * 创建一个失败的响应
     * 
     * @param retcode 错误码
     * @param message 错误信息
     * @param echo 回声
     * @return 响应对象
     */
    public static <T> ActionResponse<T> failed(Integer retcode, String message, String echo) {
        return ActionResponse.<T>builder()
                .status("failed")
                .retcode(retcode)
                .message(message)
                .echo(echo)
                .build();
    }
} 