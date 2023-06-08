# to go reactive

```xml
<!-- https://mvnrepository.com/artifact/org.mockito/mockito-core -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>3.13.0</version>
    <scope>test</scope>
</dependency>

<!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.7.2</version>
    <scope>test</scope>
</dependency>

```

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class MyServiceTest {

    @Autowired
    private MyService service;

    @MockBean
    private WebClient webClient;

    @MockBean
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @MockBean
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @MockBean
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setUp() {
        Mockito.when(webClient.post()).thenReturn(requestBodyUriSpec);
        Mockito.when(requestBodyUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.body(any())).thenReturn(requestHeadersSpec);
        Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void callApiTest_withSuccessfulResponse() {
        APIResponse mockResponse = new APIResponse("Success", HttpStatus.OK);

        Mockito.when(responseSpec.bodyToMono(APIResponse.class))
                .thenReturn(Mono.just(mockResponse));

        Mono<APIResponse> result = service.callApi("param1", 123);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(apiResponse ->
                        apiResponse.getBody().equals("Success") &&
                        apiResponse.getStatusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    @Test
    public void callApiTest_withErrorResponse() {
        APIResponse mockResponse = new APIResponse("Failure", HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(responseSpec.bodyToMono(APIResponse.class))
                .thenReturn(Mono.just(mockResponse));

        Mono<APIResponse> result = service.callApi("param1", 123);

        StepVerifier.create(result)
                .expectSubscription()
                .expectNextMatches(apiResponse ->
                        apiResponse.getBody().equals("Failure") &&
                        apiResponse.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }
}

```
- In the above tests, we're simulating a successful response and an error response from the external API. We use expectNextMatches to check the values in the APIResponse objects that the callApi method returns.

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(MyController.class)
public class MyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MyService myService;

    @Test
    public void callApiTest_withSuccessfulResponse() {
        when(myService.callApi(anyString(), anyInt()))
                .thenReturn(Mono.just(new APIResponse("Success", HttpStatus.OK)));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                .path("/api")
                .queryParam("param1", "test")
                .queryParam("param2", "123")
                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Success");
    }

    @Test
    public void callApiTest_withErrorResponse() {
        when(myService.callApi(anyString(), anyInt()))
                .thenReturn(Mono.just(new APIResponse("Failure", HttpStatus.INTERNAL_SERVER_ERROR)));

        webTestClient.get().uri(uriBuilder -> uriBuilder
                .path("/api")
                .queryParam("param1", "test")
                .queryParam("param2", "123")
                .build())
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody(String.class).isEqualTo("Failure");
    }
}


```
- In the above tests, we're simulating a successful response and an error response from the callApi method of MyService and checking the HTTP status and body of the responses from the controller.

## appendix - java code
 - in this example, the callApi method in the MyService class will first check if the cache contains a response for the given parameters. If the cache does not contain a response, the method will call the external API and put the response into the cache if the status code is in the 2xx range. The callApi method in the MyController class will return a ResponseEntity with the status code and body from the APIResponse object.
```java
@Service
public class MyService {

    @Autowired
    @Qualifier("apiResponseCache")
    private Cache apiResponseCache;

    public Mono<APIResponse> callApi(String param1, int param2) {
        // Use a consistent key
        String key = param1 + "_" + param2;
        APIResponse apiResponse = apiResponseCache.get(key, APIResponse.class);

        // If the cache does not contain the API response, call the API and put the response into the cache
        if(apiResponse == null) {
            return callExternalApi(param1, param2)
                    .doOnNext(response -> {
                        if(response.getStatusCode().is2xxSuccessful()) {
                            apiResponseCache.put(key, response);
                            log.info("API response was successful and cached");
                        } else {
                            log.warn("API response was not successful, not caching");
                        }
                    })
                    .doOnError(e -> log.error("Error calling API", e))
                    .onErrorResume(e -> Mono.just(new APIResponse("Error occurred", HttpStatus.INTERNAL_SERVER_ERROR)));
        }

        return Mono.just(apiResponse);
    }

    private Mono<APIResponse> callExternalApi(String param1, int param2) {
        // Your WebClient API call here...
    }
}


```

```java
@RestController
public class MyController {
    
    @Autowired
    private MyService myService;
    
    @GetMapping("/api")
    public Mono<ResponseEntity<String>> callApi(@RequestParam String param1, @RequestParam int param2) {
        return myService.callApi(param1, param2)
                .map(apiResponse -> {
                    if(apiResponse.getStatusCode().is2xxSuccessful()) {
                        return ResponseEntity.ok(apiResponse.getBody());
                    } else {
                        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse.getBody());
                    }
                });
    }
}

```

```java
@Service
public class MyService {

    @Autowired
    @Qualifier("apiResponseCache")
    private Cache apiResponseCache;

    public Mono<APIResponse> callApi(String param1, int param2) {
        // Use a consistent key
        String key = param1 + "_" + param2;
        APIResponse apiResponse = apiResponseCache.get(key, APIResponse.class);

        // If the cache does not contain the API response, call the API and put the response into the cache
        if(apiResponse == null) {
            return callExternalApi(param1, param2)
                    .doOnNext(response -> {
                        if(response.getStatusCode().is2xxSuccessful()) {
                            apiResponseCache.put(key, response);
                            log.info("API response was successful and cached");
                        } else {
        
```