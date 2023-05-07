package pack.solution2;

import java.util.HashMap;
import java.util.Map;

public class Human {
    public static void main(String[] args) {
        Map<String, ApiRequestBody> requestBodies = new HashMap<>();
        requestBodies.put("reference", new ApiRequestBody("reference", "abc"));
        requestBodies.put("check", new ApiRequestBody("check", "xyz [reference] [outerValue] [crossRate]"));

        CalculateAPI api = new CalculateAPI();
        Library library = new Library();
        library.processRequestBodies(requestBodies, api);

        for (ApiRequestBody requestBody : requestBodies.values()) {
            System.out.println(requestBody
                    .getName() + ": " + requestBody.getValue());
        }
    }
}
