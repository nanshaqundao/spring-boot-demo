package model;

public class AnnaCodeResponse {
    private String srcCode;
    private String annaCode;
    private String crossCode;

    public String getSrcCode() {
        return srcCode;
    }

    public void setSrcCode(String srcCode) {
        this.srcCode = srcCode;
    }

    public String getAnnaCode() {
        return annaCode;
    }

    public void setAnnaCode(String annaCode) {
        this.annaCode = annaCode;
    }

    public String getCrossCode() {
        return crossCode;
    }

    public void setCrossCode(String crossCode) {
        this.crossCode = crossCode;
    }

    public static AnnaCodeResponse fromJson(String json) {
        return new com.google.gson.Gson().fromJson(json, AnnaCodeResponse.class);
    }
}
