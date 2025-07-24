package com.sosorin.ranabot.http;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 * @Date 2024/7/19 22:46
 */
public class ResponseModel<T> extends ResponseBaseModel {

    private static final long serialVersionUID = -2938815306663747583L;

    private T data;

    public ResponseModel() {
    }

    public ResponseModel(Integer code, String message) {
        this(code, message, null);
    }

    public ResponseModel(Integer code, String message, T data) {
        this(code, message, System.currentTimeMillis(), data);
    }

    public ResponseModel(Integer code, String message, long timestamp, T data) {
        super(code, message, timestamp);
        this.data = data;
    }

    private static <T> ResponseModel<T> buildModel(HttpStatus rse) {
        return buildModel(rse.value(), rse.getReasonPhrase(), null, System.currentTimeMillis());
    }

    private static <T> ResponseModel<T> buildModel(HttpStatus rse, T data) {
        return buildModel(rse.value(), rse.getReasonPhrase(), data, System.currentTimeMillis());
    }

    private static <T> ResponseModel<T> buildModel(Integer code, String message) {
        return buildModel(code, message, null, System.currentTimeMillis());
    }

    public static <T> ResponseModel<T> buildModel(Integer code, String message, T data, long timestamp) {
        return new ResponseModel(code, message, timestamp, data);
    }

    public static <T> ResponseModel<T> SUCCESS() {
        return SUCCESS(null);
    }

    public static <T> ResponseModel<T> SUCCESS(T data) {
        return buildModel(HttpStatus.OK, data);
    }

    public static <T> ResponseModel<T> FAIL() {
        return FAIL(null);
    }

    public static <T> ResponseModel<T> FAIL(String message) {
        if (StringUtils.isBlank(message)) {
            message = HttpStatus.BAD_REQUEST.getReasonPhrase();
        }
        return buildModel(HttpStatus.BAD_REQUEST.value(), message);
    }

    public static <T> ResponseModel<T> FAIL(Object message) {
        if (ObjectUtil.isEmpty(message)) {
            message = HttpStatus.BAD_REQUEST.getReasonPhrase();
        }
        return buildModel(HttpStatus.BAD_REQUEST.value(), JSONUtil.toJsonStr(message));
    }

    public static <T> ResponseModel<T> PARAM_EMPTY() {
        return PARAM_EMPTY(null);
    }

    public static <T> ResponseModel<T> PARAM_EMPTY(String paramName) {
        if (StringUtils.isBlank(paramName)) {
            paramName = "";
        }
        return buildModel(HttpStatus.BAD_REQUEST.value(), String.format("%s参数为空", paramName));
    }

    public static <T> ResponseModel<T> PARAM_ERROR(String paramName) {
        if (StringUtils.isBlank(paramName)) {
            paramName = "";
        }
        return buildModel(HttpStatus.BAD_REQUEST.value(), String.format("%s参数错误", paramName));
    }

    public static <T> ResponseModel<T> SYSTEM_ERROR() {
        return buildModel(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseModel<T> BUILD(HttpStatus rse, T data) {
        return buildModel(rse, data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}