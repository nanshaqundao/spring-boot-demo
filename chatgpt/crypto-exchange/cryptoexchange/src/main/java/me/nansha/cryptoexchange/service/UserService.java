package me.nansha.cryptoexchange.service;

import me.nansha.cryptoexchange.model.User;
import me.nansha.cryptoexchange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Business logic here
        return userRepository.save(user);
    }
}
