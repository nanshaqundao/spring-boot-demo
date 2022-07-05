package com.example.ch05basic.config;

import com.example.ch05basic.model.User;
import com.example.ch05basic.repository.UserRepository;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {

        // functional interface implementation
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null) return user;
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        };
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
