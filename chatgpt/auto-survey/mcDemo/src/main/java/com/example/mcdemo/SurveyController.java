package com.example.mcdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @Autowired
    private SurveySubmissionService surveySubmissionService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitSurvey(@RequestBody SurveyData surveyData) {
        try {
            if (surveySubmissionService.submitSurvey(surveyData)) {
                return ResponseEntity.ok("Survey submitted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Automation submission failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit survey: " + e.getMessage());
        }
    }
}