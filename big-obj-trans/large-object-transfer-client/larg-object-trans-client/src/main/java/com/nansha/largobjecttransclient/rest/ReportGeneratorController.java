package com.nansha.largobjecttransclient.rest;

import com.nansha.largobjecttransclient.service.ReportGenerator;
import com.nansha.largobjecttransclient.service.ReportGeneratorV0;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@RestController
@RequestMapping("/report")
public class ReportGeneratorController {
    private final ReportGenerator reportGenerator;
    private final ReportGeneratorV0 reportGeneratorV0;

    public ReportGeneratorController(ReportGenerator reportGenerator, ReportGeneratorV0 reportGeneratorV0) {
        this.reportGenerator = reportGenerator;
        this.reportGeneratorV0 = reportGeneratorV0;
    }

    @GetMapping("/generate")
    public void generateReport() {
        reportGeneratorV0.fetchAndProcessPage(0);
    }

    @GetMapping("/generates")
    public Mono<ResponseEntity<Void>> generateReports() {
        return reportGenerator.fetchAndProcessPage(0, new ArrayList<>())
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(error -> {
                    System.out.println("Error fetching message states: " + error);
                    // Depending on the error, you might want to return an error response
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
