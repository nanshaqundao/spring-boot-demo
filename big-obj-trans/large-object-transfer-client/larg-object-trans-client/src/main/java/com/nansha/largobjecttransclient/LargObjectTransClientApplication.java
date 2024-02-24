package com.nansha.largobjecttransclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LargObjectTransClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(LargObjectTransClientApplication.class, args);
  }
}
