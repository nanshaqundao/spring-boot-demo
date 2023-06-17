package com.example.demo.service;

// DummyServiceTest.java

import static org.mockito.ArgumentMatchers.*;

import com.example.demo.exception.OtherException;
import com.example.demo.model.DummyWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DummyServiceTest {

    @Mock
    private CaffeineCache dummyCache;

    @Mock
    private DummyClient dummyClient;


    private DummyService dummyService;


    @BeforeEach
    void setUp() {
        dummyService = new DummyService(dummyCache, dummyClient);
        dummyCache.clear();
    }

    @Test
    public void getIsin_whenCacheIsEmpty_shouldCallClientAndCacheTheValue() {
        DummyWrapper dummyWrapper = new DummyWrapper(HttpStatus.OK, "dummy response");
        Mockito.when(dummyCache.get("dummy", DummyWrapper.class)).thenReturn(null);
        Mockito.when(dummyClient.getIsin(anyString())).thenReturn(Mono.just(dummyWrapper));


        Mono<DummyWrapper> result = dummyService.getIsin("dummy");
        StepVerifier.create(result)
                .expectNext(dummyWrapper)
                .verifyComplete();

        Mockito.verify(dummyClient).getIsin("dummy");
        Mockito.verify(dummyCache).put("dummy", dummyWrapper);
    }

    @Test
    public void getIsin_whenCacheIsNotEmpty_shouldNotCallClient() {
        DummyWrapper dummyWrapper = new DummyWrapper(HttpStatus.OK, "dummy response");
        Mockito.when(dummyCache.get("dummy", DummyWrapper.class)).thenReturn(dummyWrapper);

        StepVerifier.create(dummyService.getIsin("dummy"))
                .expectNext(dummyWrapper)
                .verifyComplete();

        Mockito.verify(dummyClient, Mockito.never()).getIsin("dummy");
    }

    @Test
    public void getIsin_whenClientFails_shouldPropagateError() {
        Mockito.when(dummyCache.get(anyString(), eq(DummyWrapper.class))).thenReturn(null);
    Mockito.when(dummyClient.getIsin(anyString())).thenReturn(Mono.error(new OtherException("dummy error")));

        StepVerifier.create(dummyService.getIsin("dummy"))
                .expectErrorMatches(throwable -> throwable instanceof OtherException)
                .verify();

        Mockito.verify(dummyClient).getIsin("dummy");
        Mockito.verify(dummyCache, Mockito.never()).put(anyString(), any());
    }

}
