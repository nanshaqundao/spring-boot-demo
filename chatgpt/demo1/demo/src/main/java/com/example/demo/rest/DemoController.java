package com.example.demo.rest;

import com.example.demo.model.Input;
import com.example.demo.service.DemoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @Operation(summary = "This is a demo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hello World!")
    })
    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello World!");
    }

    // This is a demo for openai
    @Operation(summary = "This is a demo for openai")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hello World!")
    })
    @GetMapping("/chat/{message}")
    public String chat(@PathVariable String message) throws JsonProcessingException {
        String result = demoService.generateResponse(message);
        Input input = demoService.parseResponse(result);
        return input.getChoices().get(0).getMessage().getContent();
        //return demoService.generateResponse(message);
    }


}
