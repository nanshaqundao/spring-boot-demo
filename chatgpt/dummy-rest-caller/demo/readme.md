# Practice of Reactive API Caller

## Summary
 - full reactive API caller
 - multiple layers of reactive API caller
 - proper error handling
 - production quality with timeout and retry
 - unit test with mock server and reactive
 - cache workaround with reactive (not annotation)

## full reactive API caller
 - using spring webflux
 - actual call maker is WebClient
 - WebClient instance is injected as bean defined in WebClientProvider, with config from WebClientConfig
 - connectionTimeout, readTimeout, writeTimeout and responseTimeout are set up from bean definition
 - returns Mono<T>

## multiple layers of reactive API caller
### Controller -> Service -> Client
 - Controller: API endpoint
```java
   @GetMapping("/isin")
   public Mono<ResponseEntity<String>> getIsin(@RequestParam String name) {
   return dummyService
      .getIsin(name)
      .map(response -> ResponseEntity.status(response.getStatusCode()).body(response.getBody()))
      .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Real Internal Error")));
   }
   ```
 - Service: adopting caching
```java
public Mono<DummyWrapper> getIsin(String name) {
    DummyWrapper dummyWrapper = dummyCache.get(name, DummyWrapper.class);
    if (dummyWrapper == null) {
      return dummyClient
          .getIsin(name)
          .doOnNext(
              entry -> {
                System.out.println("Caching added for " + name + " with value " + entry);
                dummyCache.put(name, entry);
              })
          .doOnError(
              error -> {
                System.out.println("doOnError: " + error);
                throw new OtherException("Other error");
              });
    }
    System.out.println(
        "Taking previously cached value for " + name + " with value " + dummyWrapper);
    return Mono.just(dummyWrapper);
  }
```
 - Client: actual call maker
```java
  public Mono<DummyWrapper> getIsin(String name) {

    return webClientWithTimeout
        .post()
        .uri((UriBuilder uriBuilder) -> uriBuilder.path("/api/isin").build())
        .body(BodyInserters.fromValue(name))
        .exchangeToMono(
            clientResponse -> {
              if (clientResponse.statusCode().is5xxServerError()
                  || clientResponse.statusCode().isSameCodeAs(HttpStatus.REQUEST_TIMEOUT)
                  || clientResponse.statusCode().isSameCodeAs(HttpStatus.TOO_MANY_REQUESTS)) {
                System.out.println(
                    "Call get isin failed with error: " + clientResponse.statusCode());
                return clientResponse
                    .bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new ServerException("Server error: " + body)));
              } else {
                return clientResponse
                    .bodyToMono(String.class)
                    .map(x -> new DummyWrapper(clientResponse.statusCode(), x));
              }
            })
        .retryWhen(retrySPec());
  }
```

## proper error handing
 - Controller: the `onErrorResume` is the place to handle error from lower layer
 - Service: throw OtherException for any error that is not due to http status code
 - Client: throw ServerException for any http status code initially, 
and then retry for 3 times if still not handled will throw OtherException


## production quality with timeout and retry
 - Retry logic is placed at the earliest place, which is the client
 - Retry logic is defined in `retrySpec()`, which contains 3 times of retry with 1 second delay
 - before each retry, have some loggings to tell user what is happening and how many times of retry left
 - Have jitter to avoid all retry at the same time
 - Have retry cap to avoid infinite retry and once reach the cap, throw OtherException

## unit test with mock server and reactive
 - Unit test to cover all layers
 - Unit test to cover good route, bad route and error route
 - Using StepVerifier to verify the result of Mono under reactive environment
 - Using MockWebServer to mock the server response
 - Using Mockito to mock the bean behaviour

## caching under reactive in spring
 - Spring does not support native caching under reactive environment, like annotation
 - Have to use workaround to achieve caching
 - In this case, use caffeine cache as a map