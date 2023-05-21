package pack.solution4.instances;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import pack.solution4.adapter.CommonRequestBodyUnitTypeAdapter;
import pack.solution4.adapter.CommonRequestUnitTypeAdapter;
import pack.solution4.api.API;
import pack.solution4.model.CommonRequestBodyUnit;
import pack.solution4.model.CommonRequestUnit;
import pack.solution4.model.FunctionalValue;

import java.util.HashMap;

public class SolutionObjectGenerator {
    public static CommonRequestBodyUnit generateCommonRequestBodyUnit(WebClient webClient) {

        CommonRequestBodyUnit requestBody = new CommonRequestBodyUnit();
        requestBody.getStringPropertiesMap().put("NotionalCurrency1", "USD");
        requestBody.getStringPropertiesMap().put("NotionalCurrency2", "EUR");
        requestBody.getFunctionPropertiesMap().put("ExpiryDate", new FunctionalValue(x -> API.apiCall(webClient, "/getExpiryDate", new HashMap<>(), "")));
        return requestBody;
    }

    public static CommonRequestUnit generateCommonRequestUnit(WebClient webClient) {
        CommonRequestUnit request = new CommonRequestUnit();
        request.setUrl("http://localhost:8080");
        request.setHttpMethod("GET");
        request.setRequestBody(generateCommonRequestBodyUnit(webClient));
        return request;
    }

    public static String getRequestJson(CommonRequestUnit commonRequestUnit) {
        CommonRequestBodyUnitTypeAdapter requestBodyAdapter = new CommonRequestBodyUnitTypeAdapter();
        Gson gson = new GsonBuilder().registerTypeAdapter(CommonRequestUnit.class, new CommonRequestUnitTypeAdapter(requestBodyAdapter)).registerTypeAdapter(CommonRequestBodyUnit.class, new CommonRequestBodyUnitTypeAdapter()).excludeFieldsWithoutExposeAnnotation().create();
        String requestJson = gson.toJson(commonRequestUnit);
        System.out.println("requestJson: " + requestJson);
        return requestJson;
    }
}
