package com.nansha.largobjecttransclient.rest;

import com.nansha.largobjecttransclient.service.ReportGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportGeneratorController {
    private final ReportGenerator reportGenerator;

    public ReportGeneratorController(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @GetMapping("/generate")
    public void generateReport() {
        reportGenerator.fetchAndProcessPage(0);
    }
}
