package pack.solution2;

public class ApiRequest {
    private String url;
    private String requestBodyJson;

    public ApiRequest(String url, String requestBodyJson) {
        this.url = url;
        this.requestBodyJson = requestBodyJson;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRequestBodyJson() {
        return requestBodyJson;
    }

    public void setRequestBodyJson(String requestBodyJson) {
        this.requestBodyJson = requestBodyJson;
    }
}
