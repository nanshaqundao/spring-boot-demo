package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Demo2Application {
    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(Demo2Application.class);
        application.addInitializers(new LearnApplicationContextInitializer());
        ConfigurableApplicationContext context = application.run(args);
        ArgsBean bean = context.getBean(ArgsBean.class);

        bean.printArgs();
    }
}
