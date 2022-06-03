package com.example.ch10ex1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewController {
    @GetMapping("/newHello")
    public String hello(){
        return "Hello!";
    }
}
