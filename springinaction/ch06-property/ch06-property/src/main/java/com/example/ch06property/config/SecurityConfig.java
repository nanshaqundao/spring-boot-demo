package com.example.ch06property.config;


import com.example.ch06property.model.TacoUser;
import com.example.ch06property.repository.TacoUserRepository;
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
                .authorizeRequests()
                    .antMatchers("/design", "/orders").access("hasRole('USER')")
                    .antMatchers("/", "/**", "/h2-console/**").access("permitAll()")
                // needed for h2-console
                .and()
                    .csrf()
                    .ignoringAntMatchers("/h2-console/**")
                .and()
                    .formLogin().loginPage("/login")
                    .defaultSuccessUrl("/design")
                // needed for h2-console
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()
                .and().build();
    }
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
////        List<UserDetails> userDetailsList = new ArrayList<>();
////        userDetailsList.add(new User("jack", passwordEncoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
////        userDetailsList.add(new User("luke", passwordEncoder.encode("password"), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))));
////        return new InMemoryUserDetailsManager(userDetailsList);
////
//        return null;
//
//    }
}
