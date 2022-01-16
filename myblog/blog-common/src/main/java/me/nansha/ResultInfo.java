package me.nansha;

public enum ResultInfo {
    SUCCESS("200","operation successful"),
    NOT_FOUND("404", "page not found"),
    GLOBAL_ERROR("503","a global error"),
    ;

    private String code;
    private String message;

    ResultInfo(String code, String message) {
        this.code = code;
        this.message = message;
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
}
