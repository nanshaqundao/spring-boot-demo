package com.nanshaqundao.jpademo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.swing.text.html.parser.Entity;

@SpringBootApplication
public class JpademoApplication {

	@Autowired
	GenericEntityRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(JpademoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(GenericEntityRepository repository) {
		return (args) -> {
			// save a few customers
			GenericEntity newEntity = new GenericEntity("jack");
			repository.save(newEntity);



			// fetch an individual customer by ID
			GenericEntity foundEntity = repository.getOne(newEntity.getId());
			System.out.println("hi");



		};
	}
}
