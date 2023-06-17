package com.example.demo.rest;

// DummyControllerTest.java
import com.example.demo.model.DummyWrapper;
import com.example.demo.service.DummyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = DummyController.class)
public class DummyControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DummyService dummyService;

    @Test
    public void getIsin_whenServiceReturnsValidData_shouldReturn200() {
        DummyWrapper dummyWrapper = new DummyWrapper(HttpStatus.OK, "dummy response");
        Mockito.when(dummyService.getIsin(Mockito.anyString())).thenReturn(Mono.just(dummyWrapper));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/dummy/isin").queryParam("name", "dummy").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("dummy response");
    }

    @Test
    public void getIsin_whenServiceReturnsError_shouldReturn500() {
        Mockito.when(dummyService.getIsin(Mockito.anyString())).thenReturn(Mono.error(new RuntimeException()));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/dummy/isin").queryParam("name", "dummy").build())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Real Internal Error");
    }
}
