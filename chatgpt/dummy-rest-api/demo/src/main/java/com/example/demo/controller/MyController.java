package com.example.demo.controller;

import com.example.demo.model.RefResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MyController {
    @GetMapping("/refData")
    public String getRefData(String srcCode) {
        return "refData";
    }

    @GetMapping("/annaData")
    public ResponseEntity<RefResponse> getAnnaData(String srcCode) {
        RefResponse refResponse = new RefResponse();
        refResponse.setSrcCode(srcCode);
        refResponse.setAnnaCode("annaCode" + srcCode);
        refResponse.setCrossCode("crossCode" + srcCode);
        return ResponseEntity.ok(refResponse);
    }

    @PostMapping("/isin")
    public String getIsin(@RequestBody String requestBody) {
        return "isin-" + requestBody;
    }
}
