package config;

import model.Parrot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    Parrot parrot(){
        var p = new Parrot();
        p.setName("Koko");
        System.out.println("---Bean initialised with Koko name from ProjectConfig---");
        return p;
    }

    @Bean
    String hello() {
        return "hello";
    }

    @Bean
    Integer ten(){
        return 10;
    }
}
