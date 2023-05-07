package pack.solution3.model3;

import java.util.Map;

public class CommonRequestUnit {
    private String url;
    private String httpMethod;
    private Map<String, String> parametersMap;

    private CommonRequestBodyUnit requestBody;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Map<String, String> getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(Map<String, String> parametersMap) {
        this.parametersMap = parametersMap;
    }

    public CommonRequestBodyUnit getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(CommonRequestBodyUnit requestBody) {
        this.requestBody = requestBody;
    }
}
