package demo;


import adapter.CommonRequestBodyUnitTypeAdapter;
import adapter.CommonRequestUnitTypeAdapter;
import api.API;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import instances.SolutionObjectGenerator;
import model.CommonRequestBodyUnit;
import model.CommonRequestUnit;
import org.springframework.web.reactive.function.client.WebClient;

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
