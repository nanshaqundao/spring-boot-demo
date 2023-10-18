package me.nansha.cryptoexchange.controller;

import me.nansha.cryptoexchange.dto.LoginRequest;
import me.nansha.cryptoexchange.dto.LoginResponse;
import me.nansha.cryptoexchange.dto.RegisterRequest;
import me.nansha.cryptoexchange.dto.RegisterResponse;
import me.nansha.cryptoexchange.service.UserAuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/users")
public class UserAuthenticationController {

  private final UserAuthenticationService userAuthenticationService;

  public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
    this.userAuthenticationService = userAuthenticationService;
  }

  @PostMapping("/register")
  public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
    userAuthenticationService.register(registerRequest);
    return new RegisterResponse("User registered successfully");
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    return userAuthenticationService.login(loginRequest);
  }
}
