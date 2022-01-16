package me.nansha;


import java.util.HashMap;
import java.util.Map;

public class Result {
    /**
     * return successful or failed
     */
    private boolean status;

    /**
     * status code
     */
    private String code;

    /**
     * message information
     */

    private String message;

    Map<String, Object> data = new HashMap<>();

    public Result() {
    }

    public static Result success() {
        Result result = new Result();
        result.setStatus(true);
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setStatus(false);
        return result;
    }

    public Result code(String code) {
        this.setCode(code);
        return this;
    }

    public Result message(String message) {
        this.setMessage(message);
        return this;
    }


    public Result codeAndMessage(ResultInfo resultInfo) {
        this.setCode(resultInfo.getCode());
        this.setMessage(resultInfo.getMessage());
        return this;
    }
    public Result codeAndMessage(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
        return this;
    }

    public Result data(String key, Object value) {
        this.getData().put(key, value);
        return this;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


}
