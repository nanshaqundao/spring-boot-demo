package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;

@Order(123) // the lower the earlier to execute
public class LearnApplicationContextInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("---====Container initialized bean count: " + applicationContext.getBeanDefinitionCount());
    }
}
