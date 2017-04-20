package com.jack.okhttp.base;

/**
 * Created by jackzhous on 2017/4/20.
 */

public class BaseResponse<T> {
    private int code;
    private String message;
    private T detail;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getDetail() {
        return detail;
    }

    public void setDetail(T detail) {
        this.detail = detail;
    }
}
