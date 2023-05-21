package demo;


import adapter.CommonRequestBodyUnitTypeAdapter;
import adapter.CommonRequestUnitTypeAdapter;
import api.API;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import helper.APIHelper;
import instances.SolutionObjectGenerator;
import model.AnnaCodeResponse;
import model.CommonRequestBodyUnit;
import model.CommonRequestUnit;
import org.springframework.web.reactive.function.client.WebClient;

public class SimpleIsinDemo {
    public static void main(String[] args) {
        System.out.println("start\n\n");
        WebClient webClient = WebClient.create("http://localhost:8080");
        CommonRequestUnit isinRequest = SolutionObjectGenerator.generateIsinCommonRequest(webClient);
        CommonRequestBodyUnit isinRequestBody = isinRequest.getRequestBody();


        CommonRequestBodyUnitTypeAdapter requestBodyAdapter = new CommonRequestBodyUnitTypeAdapter();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(CommonRequestUnit.class, new CommonRequestUnitTypeAdapter(requestBodyAdapter))
                .registerTypeAdapter(CommonRequestBodyUnit.class, new CommonRequestBodyUnitTypeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        System.out.println("prior the call to gson.toJson() the request body is: " + new Gson().toJson(isinRequestBody));

        System.out.println();
        System.out.println("----------Call the special gson.toJson()-------------");
        System.out.println();
        String requestJson = gson.toJson(isinRequestBody);
        System.out.println("call gson.toJson() will trigger the serialisation and trigger the API call to modify the request body: " + requestJson);


        String isinResponse = API.apiPostCall(webClient, isinRequest.getUrl(), isinRequest.getParametersMap(), requestJson);
        System.out.println();
        System.out.println(isinResponse);

//        CommonRequestUnit annaDataRequest = SolutionObjectGenerator.generateAnnaDataCommonRequest();
//        CommonRequestBodyUnit annaDataRequestBody = annaDataRequest.getRequestBody();
//        AnnaCodeResponse response = API.apiAnnaCall(webClient, "http://localhost:8080/api/annaData", annaDataRequest.getParametersMap(), "");

        System.out.println("end");
    }
}
