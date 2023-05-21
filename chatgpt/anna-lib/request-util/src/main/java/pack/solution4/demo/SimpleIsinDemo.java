package pack.solution4.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mock.FxIsinMock;
import org.springframework.web.reactive.function.client.WebClient;
import pack.solution4.adapter.CommonRequestBodyUnitTypeAdapter;
import pack.solution4.adapter.CommonRequestUnitTypeAdapter;
import pack.solution4.api.API;
import pack.solution4.instances.SolutionObjectGenerator;
import pack.solution4.model.CommonRequestBodyUnit;
import pack.solution4.model.CommonRequestUnit;

public class SimpleIsinDemo {
    public static void main(String[] args) {
        WebClient webClient = WebClient.create("http://localhost:8080");
        CommonRequestUnit isinRequest = SolutionObjectGenerator.generateIsinCommonRequest(webClient);
        CommonRequestBodyUnit isinRequestBody = isinRequest.getRequestBody();

        CommonRequestUnit refDataRequest = SolutionObjectGenerator.generateRefDataCommonRequest();
        CommonRequestBodyUnitTypeAdapter requestBodyAdapter = new CommonRequestBodyUnitTypeAdapter();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommonRequestUnit.class, new CommonRequestUnitTypeAdapter(requestBodyAdapter))
                .registerTypeAdapter(CommonRequestBodyUnit.class, new CommonRequestBodyUnitTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        String requestJson = gson.toJson(isinRequestBody);
        System.out.println(requestJson);

        System.out.println("hi");

        String isinResponse = API.apiPostCall(webClient, isinRequest.getUrl(), isinRequest.getParametersMap(), requestJson);
        System.out.println(isinResponse);
        System.out.printf("end");
    }
}
