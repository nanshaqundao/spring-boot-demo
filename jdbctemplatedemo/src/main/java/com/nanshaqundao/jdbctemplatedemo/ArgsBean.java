package com.nanshaqundao.jdbctemplatedemo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ArgsBean {
    @Resource
    private ApplicationArguments arguments;

    public void printArgs() {
        System.out.println("## Non optional parameter: " + arguments.getNonOptionArgs().size());
        System.out.println("## Optional parameters: " + arguments.getOptionNames().size());

        System.out.println("====Detailed Non optional parameters===");
        arguments.getNonOptionArgs().forEach(System.out::println);

        System.out.println("===Detailed Optional parameters===");
        arguments.getOptionNames().forEach(name ->{
            System.out.println("---" + name + "="+arguments.getOptionValues(name));
        });
    }
}
