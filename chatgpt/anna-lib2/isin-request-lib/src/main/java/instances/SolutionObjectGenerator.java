package instances;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import adapter.CommonRequestBodyUnitTypeAdapter;
import adapter.CommonRequestUnitTypeAdapter;
import api.API;
import model.CommonRequestBodyUnit;
import model.CommonRequestUnit;
import model.FunctionalValue;

public class SolutionObjectGenerator {
    public static CommonRequestBodyUnit generateIsinCommonRequestBody(WebClient webClient) {

        CommonRequestBodyUnit requestBody = new CommonRequestBodyUnit();
        requestBody.getStringPropertiesMap()
                .put("NotionalCurrency1", "USD");
        requestBody.getStringPropertiesMap()
                .put("NotionalCurrency2", "EUR");
        requestBody.getFunctionPropertiesMap()
                .put("ExpiryDate",
                        new FunctionalValue(x ->
                                API.apiGetCall(webClient, "http://localhost:8080/api/refData", generateRefDataRequestBody().getStringPropertiesMap())));
        return requestBody;
    }

    public static CommonRequestUnit generateIsinCommonRequest(WebClient webClient) {
        CommonRequestUnit request = new CommonRequestUnit();
        request.setUrl("/api/isin");
        request.setHttpMethod("POST");
        request.setRequestBody(generateIsinCommonRequestBody(webClient));
        return request;
    }

    public static CommonRequestBodyUnit generateRefDataRequestBody() {
        CommonRequestBodyUnit requestBody = new CommonRequestBodyUnit();
        requestBody.getStringPropertiesMap()
                .put("srcCode", "USD");
        return requestBody;
    }

    public static CommonRequestUnit generateRefDataCommonRequest() {
        CommonRequestUnit request = new CommonRequestUnit();
        request.setUrl("/api/refData");
        request.setHttpMethod("GET");
        request.setRequestBody(generateRefDataRequestBody());
        return request;
    }

    public static CommonRequestBodyUnit generateAnnaDataRequestBody() {
        CommonRequestBodyUnit requestBody = new CommonRequestBodyUnit();
        requestBody.getStringPropertiesMap()
                .put("srcCode", "USD");
        return requestBody;
    }

    public static CommonRequestUnit generateAnnaDataCommonRequest() {
        CommonRequestUnit request = new CommonRequestUnit();
        request.setUrl("/api/annaData");
        request.setHttpMethod("GET");
        request.getParametersMap()
                .put("srcCode", "ab");
        request.setRequestBody(generateAnnaDataRequestBody());
        return request;
    }

    public static String getRequestJson(CommonRequestUnit commonRequestUnit) {
        CommonRequestBodyUnitTypeAdapter requestBodyAdapter = new CommonRequestBodyUnitTypeAdapter();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommonRequestUnit.class, new CommonRequestUnitTypeAdapter(requestBodyAdapter))
                .registerTypeAdapter(CommonRequestBodyUnit.class, new CommonRequestBodyUnitTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String requestJson = gson.toJson(commonRequestUnit);
        System.out.println("requestJson: " + requestJson);
        return requestJson;
    }
}
