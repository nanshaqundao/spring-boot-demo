package me.nansha.cryptoexchange.security;

import java.time.Instant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtProvider {
  private final JwtEncoder jwtEncoder;
  public static final Long EXPIRATION_TIME_IN_SECONDS = 3600L;

  public JwtProvider(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
  }

  public String generateToken(Authentication authentication) {
    User principal = (User) authentication.getPrincipal();
    var now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(EXPIRATION_TIME_IN_SECONDS))
            .subject(principal.getUsername())
            .claim("scope", "ROLE_USER")
            .build();

    return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }
}
