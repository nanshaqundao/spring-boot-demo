package com.nansha.largobjecttransclient.rest;

import com.nansha.largobjecttransclient.model.UploadResult;
import com.nansha.largobjecttransclient.service.ReportGenerator;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
        .generateCombinedReport()
        .map(ResponseEntity::ok)
        .onErrorResume(
            error -> {
              logger.error("Error fetching message states: " + error);
              // Depending on the error, you might want to return an error response
              return Mono.just(
                  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                      .body(new UploadResult("bad", false)));
            });
  }
}
