package main;

import config.ProjectConfig;
import model.Parrot;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        System.out.println(" checking if parrot is in context...........");
        Parrot p = context.getBean(Parrot.class);
        System.out.println(p.getName() + " should be the name: Koko");
    }
}
