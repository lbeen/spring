package com.lbeen.spring.common.bean;

public class Result {
    private final int code;
    private final String msg;
    private final Object data;

    public static Result success() {
        return successMsg(null);
    }

    public static Result saveSuccess() {
        return successMsg("保存成功");
    }

    public static Result successMsg(String msg) {
        return new Result(0, msg, null);
    }

    public static Result success(Object data) {
        return new Result(0, null, data);
    }

    public static Result error() {
        return new Result(-1, "操作失败", null);
    }

    public static Result error(String msg) {
        return new Result(-1, msg, null);
    }

    private Result(int code, String msg, Object data) {
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
