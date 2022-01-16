package me.nansha.handler.exception;

import me.nansha.ResultInfo;

public class MyRuntimeException extends RuntimeException{
    private String code;
    private String message;

    public MyRuntimeException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public MyRuntimeException(ResultInfo resultInfo) {
        this.code = resultInfo.getCode();
        this.message = resultInfo.getMessage();
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
