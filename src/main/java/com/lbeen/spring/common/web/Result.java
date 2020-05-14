package com.lbeen.spring.common.web;

public class Result {
    private int code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(0, null, null);
    }

    public static Result success(Object data) {
        return new Result(0, null, data);
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }
}
