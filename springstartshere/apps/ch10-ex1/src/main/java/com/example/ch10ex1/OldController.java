package com.example.ch10ex1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OldController {
    @GetMapping("/oldHello")
    @ResponseBody
    public String hello(){
        return "Hello!";
    }

    @GetMapping("/oldCiao")
    @ResponseBody
    public String ciao(){
        return "ciao!";
    }
}
