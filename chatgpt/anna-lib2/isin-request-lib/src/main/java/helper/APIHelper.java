package helper;

import api.API;
import model.AnnaCodeResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class APIHelper {
    private AnnaCodeResponse annaCodeResponse;

    public AnnaCodeResponse getAnnaCodeResponse(WebClient webClient, String url,
                                                Map<String,String> paramMap, String body) {
        if (annaCodeResponse == null) {
            annaCodeResponse = API.apiAnnaCall(webClient, url, paramMap, body);
        }
        return annaCodeResponse;
    }
}
