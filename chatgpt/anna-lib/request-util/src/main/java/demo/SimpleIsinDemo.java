package demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mock.FxIsinMock;
import pack.solution3.adapter.CommonRequestUnitTypeAdapter;
import pack.solution3.model3.CommonRequestBodyUnit;
import pack.solution3.model3.CommonRequestUnit;

public class SimpleIsinDemo {
    public static void main(String[] args) {
        CommonRequestUnit mockRequest = FxIsinMock.getMockRequest();
//        CommonRequestBodyUnit mockRequestBody = mockRequest.getRequestBody();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommonRequestUnit.class, new CommonRequestUnitTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String mockRequestJson = gson.toJson(mockRequest);
//        String mockRequestBodyJson = gson.toJson(mockRequestBody);

        System.out.println("mockRequestJson: " + mockRequestJson);
//        System.out.println("mockRequestBodyJson: " + mockRequestBodyJson);
    }
}
