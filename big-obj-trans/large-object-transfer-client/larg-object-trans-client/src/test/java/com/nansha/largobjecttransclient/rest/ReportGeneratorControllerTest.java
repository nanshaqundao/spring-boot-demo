package com.nansha.largobjecttransclient.rest;


import com.nansha.largobjecttransclient.model.UploadResult;
import com.nansha.largobjecttransclient.service.ReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

class ReportGeneratorControllerTest {

    @Mock
    private ReportGenerator reportGenerator;

    @InjectMocks
    private ReportGeneratorController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

  //    @Test
  //    void generateReports_Success() {
  //        UploadResult uploadResult = new UploadResult("report.csv", true);
  //        when(reportGenerator.fetchAndProcessPage(0, new
  // ArrayList<>())).thenReturn(Mono.just(uploadResult));
  //
  //        StepVerifier.create(controller.generateReports())
  //                .expectNext(ResponseEntity.ok(uploadResult))
  //                .verifyComplete();
  //    }
  //
  //    @Test
  //    void generateReports_Error() {
  //        when(reportGenerator.fetchAndProcessPage(0, new ArrayList<>()))
  //                .thenReturn(Mono.error(new RuntimeException("Error during processing")));
  //
  //        StepVerifier.create(controller.generateReports())
  //                .expectNextMatches(response ->
  //                        response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR &&
  //                                response.getBody() != null &&
  //                                !response.getBody().isSuccess() &&
  //                                "bad".equals(response.getBody().getFileName()))
  //                .verifyComplete();
  //    }
}
