package org.qinyu.util;

public record Result<T>(Integer code, String msg, T data) {

    private static final String OK = " o(*≧▽≦)ツ┏━┓";
    private static final String NO = " ε(┬┬﹏┬┬)3";

    private static String setMsg(Code code, String msg) {
        return msg.endsWith(OK) || msg.endsWith(NO) ?
                msg : code.getCode().toString().startsWith("2") ? msg + OK : msg + NO;
    }

    public static <T> Result<T> ok(String msg) {
        return new Result<>(Code.OK.getCode(), setMsg(Code.OK, msg), null);
    }

    public static <T> Result<T> ok(String msg, T data) {
        return new Result<>(Code.OK.getCode(), setMsg(Code.OK, msg), data);
    }

    public static <T> Result<T> no(String msg) {
        return new Result<>(Code.NO.getCode(), setMsg(Code.NO, msg), null);
    }
}

