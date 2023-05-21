package api;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.stream.Collectors;

public class API {
    public static String apiPostCall(WebClient client, String url, Map<String, String> params, String jsonRequestBody) {
        String uri = url + params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&", "?", ""));
        return client.post()
                .uri(uri)
                .body(BodyInserters.fromValue(jsonRequestBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public static String apiGetCall(WebClient client, String url, Map<String, String> params) {
        String uri = url + params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&", "?", ""));

        return client.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


}
