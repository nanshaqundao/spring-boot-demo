package com.nansha.largobjecttransclient.rest;

import com.nansha.largobjecttransclient.model.MessageState;
import com.nansha.largobjecttransclient.model.UploadResult;
import com.nansha.largobjecttransclient.service.ReportGenerator;
import com.nansha.largobjecttransclient.service.ReportGeneratorV0;
import com.nansha.largobjecttransclient.service.ReportGeneratorV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportGeneratorController {
  private static final Logger logger = LoggerFactory.getLogger(ReportGeneratorController.class);
  private final ReportGenerator reportGenerator;

  public ReportGeneratorController(ReportGenerator reportGenerator) {
    this.reportGenerator = reportGenerator;
  }

  @GetMapping("/generates")
  public Mono<ResponseEntity<UploadResult>> generateReports() {
    return reportGenerator
        .fetchAndProcessPage(0, new ArrayList<>())
        .map(ResponseEntity::ok)
        .onErrorResume(
            error -> {
              System.out.println("Error fetching message states: " + error);
              // Depending on the error, you might want to return an error response
              return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
  }
}
