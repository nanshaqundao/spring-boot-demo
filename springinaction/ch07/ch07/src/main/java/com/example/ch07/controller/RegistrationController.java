package com.example.ch07.controller;

import com.example.ch07.model.RegistrationForm;
import com.example.ch07.repository.TacoUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final TacoUserRepository tacoUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(TacoUserRepository tacoUserRepository, PasswordEncoder passwordEncoder) {
        this.tacoUserRepository = tacoUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String registerForm() {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm registrationForm) {
        tacoUserRepository.save(registrationForm.toTacoUser(passwordEncoder));
        return "redirect:/login";
    }
}
