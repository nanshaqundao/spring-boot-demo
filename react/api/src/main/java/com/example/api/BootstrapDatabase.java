package com.example.api;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BootstrapDatabase implements CommandLineRunner {
  private final ClientRepository clientRepository;
  private final Faker faker = new Faker(Locale.getDefault());

  public BootstrapDatabase(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    for (int i = 0; i < 10; i++) {
      clientRepository.save(new Client(faker.name().fullName(), faker.internet().emailAddress()));
    }
  }
}
