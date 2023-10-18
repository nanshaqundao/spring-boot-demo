package me.nansha.cryptoexchange.service;

import java.util.Collection;
import java.util.Collections;
import me.nansha.cryptoexchange.model.User;
import me.nansha.cryptoexchange.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    var user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new org.springframework.security
            .core.userdetails.User(user.getUsername(), user.getPassword(),
            true, true, true,
            true, getAuthorities(user));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(User user) {
    return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
  }
}
