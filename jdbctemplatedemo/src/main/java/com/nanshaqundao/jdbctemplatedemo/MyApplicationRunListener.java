package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.env.ConfigurableEnvironment;

public class MyApplicationRunListener implements SpringApplicationRunListener {
    public MyApplicationRunListener(SpringApplication application, String[] args){
        System.out.println("MyApplicationRunListener constructed function");
    }
    @Override
    public void starting(){
        System.out.println("starting...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment){
        System.out.println("environmentPrepared...");
    }
}
