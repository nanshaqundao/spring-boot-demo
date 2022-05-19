package com.app.main;


import com.app.config.ProjectConfig;
import com.app.model.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

        Person person = context.getBean(Person.class);
        System.out.println("Person's name: "+ person.getName());
        System.out.println("Parrot's name: " + person.getParrot().getName());

    }
}
