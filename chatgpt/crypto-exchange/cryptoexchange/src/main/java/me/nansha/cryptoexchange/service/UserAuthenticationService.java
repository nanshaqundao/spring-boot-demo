package me.nansha.cryptoexchange.service;

import me.nansha.cryptoexchange.dto.LoginRequest;
import me.nansha.cryptoexchange.dto.LoginResponse;
import me.nansha.cryptoexchange.dto.RegisterRequest;
import me.nansha.cryptoexchange.model.User;
import me.nansha.cryptoexchange.repository.UserRepository;
import me.nansha.cryptoexchange.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;

  public UserAuthenticationService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager,
      JwtProvider jwtProvider) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtProvider = jwtProvider;
  }

  public void register(RegisterRequest registerRequest) {
    var user = User.fromRegisterRequest(registerRequest);
    var userCandidate = findByUsername(user.getUsername());
    if (userCandidate.isPresent()) {
      throw new RuntimeException("User already exists");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public LoginResponse login(LoginRequest loginRequest) {
    Authentication authenticate =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password()));
    String token = jwtProvider.generateToken(authenticate);
    return new LoginResponse(token);
  }
}
