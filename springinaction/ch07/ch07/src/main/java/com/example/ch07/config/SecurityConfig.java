package com.example.ch07.config;


import com.example.ch07.model.TacoUser;
import com.example.ch07.repository.TacoUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(TacoUserRepository tacoUserRepository) {

        // functional interface implementation
        return username -> {
            TacoUser tacoUser = tacoUserRepository.findByUsername(username);
            if (tacoUser != null) return tacoUser;
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests()
                .requestMatchers("/design","/orders").hasRole("USER")
                .requestMatchers("/", "/**", "/h2-console/**").permitAll()
                // needed for h2-console
                .and()
                    .csrf()
                .disable()

                    .formLogin().loginPage("/login")
                    .defaultSuccessUrl("/design")
                // needed for h2-console
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                .and().build();
    }
}
