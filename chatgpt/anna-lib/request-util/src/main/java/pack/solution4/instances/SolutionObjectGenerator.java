package pack.solution4.instances;

import org.springframework.web.reactive.function.client.WebClient;
import pack.solution4.model.CommonRequestBodyUnit;
import pack.solution4.model.FunctionalValue;

public class SolutionObjectGenerator {
    public static CommonRequestBodyUnit generateCommonRequestBodyUnit(WebClient webClient) {

        CommonRequestBodyUnit requestBody = new CommonRequestBodyUnit();
        requestBody.getStringPropertiesMap().put("NotionalCurrency1", "USD");
        requestBody.getStringPropertiesMap().put("NotionalCurrency2", "EUR");
        requestBody.getFunctionPropertiesMap().put("ExpiryDate",
                new FunctionalValue(x -> API.apiCall(webClient, "/getExpiryDate", new HashMap<>(), "")));
    }
}
