package demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mock.FxIsinMock;
import pack.solution3.adapter.CommonRequestBodyUnitTypeAdapter;
import pack.solution3.adapter.CommonRequestUnitTypeAdapter;
import pack.solution3.config.APIConfig;
import pack.solution3.config.PropertiesLoader;
import pack.solution3.model3.CommonRequestBodyUnit;
import pack.solution3.model3.CommonRequestUnit;

public class SimpleIsinDemo {
    public static void main(String[] args) {
        CommonRequestUnit mockRequest = FxIsinMock.getMockRequest();
        CommonRequestBodyUnitTypeAdapter requestBodyAdapter = new CommonRequestBodyUnitTypeAdapter();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommonRequestUnit.class, new CommonRequestUnitTypeAdapter(requestBodyAdapter))
                .registerTypeAdapter(CommonRequestBodyUnit.class, new CommonRequestBodyUnitTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String mockRequestJson = gson.toJson(mockRequest);


        System.out.println("mockRequestJson: " + mockRequestJson);

        CommonRequestBodyUnit mockRequestBody = mockRequest.getRequestBody();
        String mockRequestBodyJson = gson.toJson(mockRequestBody);
        System.out.println("mockRequestBodyJson: " + mockRequestBodyJson);

        APIConfig config = PropertiesLoader.getAPIConfig();
        System.out.println("config: " + config.getBaseUrl() + config.getEndpoint());
    }
}
