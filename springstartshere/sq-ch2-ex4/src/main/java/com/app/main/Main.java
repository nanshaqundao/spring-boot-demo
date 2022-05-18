package com.app.main;


import com.app.config.ProjectConfig;
import com.app.model.Parrot;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
        Parrot x = new Parrot();
        x.setName("nan");

        Supplier<Parrot> parrotSupplier = () -> x;
        context.registerBean("nan", Parrot.class, parrotSupplier);

        System.out.println(" checking if parrot is in context...........");
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName());
        System.out.println(p.getName() + " should be the name: nan");
    }
}
