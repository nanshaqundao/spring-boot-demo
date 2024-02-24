package com.nansha.largobjecttransclient.rest;

import com.nansha.largobjecttransclient.model.MessageState;
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
  private final ReportGeneratorV0 reportGeneratorV0;
  private final ReportGeneratorV1 reportGeneratorV1;

  public ReportGeneratorController(
      ReportGenerator reportGenerator,
      ReportGeneratorV0 reportGeneratorV0,
      ReportGeneratorV1 reportGeneratorV1) {
    this.reportGenerator = reportGenerator;
    this.reportGeneratorV0 = reportGeneratorV0;
    this.reportGeneratorV1 = reportGeneratorV1;
  }

  @GetMapping("/generate")
  public void generateReport() {
    reportGeneratorV0.fetchAndProcessPage(0);
  }

  @GetMapping("/generates")
  public Mono<ResponseEntity<Void>> generateReports() {
    return reportGenerator
        .fetchAndProcessPage(0, new ArrayList<>())
        .then(Mono.just(ResponseEntity.ok().<Void>build()))
        .onErrorResume(
            error -> {
              System.out.println("Error fetching message states: " + error);
              // Depending on the error, you might want to return an error response
              return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
            });
  }

  @GetMapping("/generatev1")
  public Mono<ResponseEntity<List<MessageState>>> generateReportsV1() {
    return reportGeneratorV1
        .fetchAndProcessPages(0, new ArrayList<>())
        .map(ResponseEntity::ok) // Return the accumulated list of states
        .defaultIfEmpty(ResponseEntity.ok(new ArrayList<>())) // Handle case of no data
        .onErrorResume(
            error -> {
              // Log and handle errors, returning an appropriate response
              logger.error("Error during report generation: ", error);
              return Mono.just(
                  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>()));
            });
  }
}
