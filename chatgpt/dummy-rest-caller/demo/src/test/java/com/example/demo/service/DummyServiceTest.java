package com.example.demo.service;

// DummyServiceTest.java
import com.example.demo.model.DummyWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DummyServiceTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private DummyClient dummyClient;

    @InjectMocks
    private DummyService dummyService;

    @Test
    public void getIsin_whenCacheIsEmpty_shouldCallClientAndCacheTheValue() {
        DummyWrapper dummyWrapper = new DummyWrapper(HttpStatus.OK, "dummy response");
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString(), DummyWrapper.class)).thenReturn(null);
        Mockito.when(dummyClient.getIsin(anyString())).thenReturn(Mono.just(dummyWrapper));

        StepVerifier.create(dummyService.getIsin("dummy"))
                .expectNext(dummyWrapper)
                .verifyComplete();

        Mockito.verify(dummyClient).getIsin("dummy");
        Mockito.verify(cache).put("dummy", dummyWrapper);
    }

    @Test
    public void getIsin_whenCacheIsNotEmpty_shouldNotCallClient() {
        DummyWrapper dummyWrapper = new DummyWrapper(HttpStatus.OK, "dummy response");
        Mockito.when(cacheManager.getCache(anyString())).thenReturn(cache);
        Mockito.when(cache.get(anyString(), DummyWrapper.class)).thenReturn(dummyWrapper);

        StepVerifier.create(dummyService.getIsin("dummy"))
                .expectNext(dummyWrapper)
                .verifyComplete();

        Mockito.verify(dummyClient, Mockito.never()).getIsin("dummy");
    }
}
