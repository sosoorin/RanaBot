package com.sosorin.ranabot;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 响应基础类
 */
public class ResponseBaseModel implements Serializable {

    private static final long serialVersionUID = 2900688160015741603L;

    @Schema(description = "返回状态码，更多返回错误代码请看首页的错误代码描述")
    private Integer code;//返回状态码
    @Schema(description = "返回信息，如非空，为错误原因")
    private String message;//返回信息 返回信息，如非空，为错误原因
    @Schema(description = "返回时间戳")
    private Long timestamp = System.currentTimeMillis();

    public ResponseBaseModel() {
    }

    public ResponseBaseModel(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseBaseModel(Integer code, String message, Long timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}