package pack.solution3.model3;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

public class CommonRequestUnit {
    @Expose
    private String url;
    @Expose
    private String httpMethod;
    @Expose
    private Map<String, String> parametersMap = new HashMap<>();
    @Expose
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
