package me.nansha.cryptoexchange.security;

import java.time.Instant;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import me.nansha.cryptoexchange.config.JwtConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtProvider {
  private static final Logger log = LoggerFactory.getLogger(JwtProvider.class);

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  private final JwtConfigProperties jwtConfigProperties;
  public static final Long EXPIRATION_TIME_IN_SECONDS = 3600L;

  public JwtProvider(
      JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, JwtConfigProperties jwtConfigProperties) {
    this.jwtEncoder = jwtEncoder;
    this.jwtDecoder = jwtDecoder;
    this.jwtConfigProperties = jwtConfigProperties;
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

  public String validateAndGetSubject(String token) {
    var a = this.jwtDecoder.decode(token);
    return a.getSubject();
  }

  public boolean validateToken(String token) {
    //    try {
    //      Jws<Claims> claims =
    //
    // Jwts.parser().verifyWith(jwtConfigProperties.getPrivateKey().getEncoded()).build().parseSignedClaims(token);
    //      //  parseClaimsJws will check expiration date. No need do here.
    //      log.info("expiration date: {}", claims.getPayload().getExpiration());
    //      return true;
    //    } catch (JwtException | IllegalArgumentException e) {
    //      log.error("Invalid JWT token: {}", e.getMessage());
    //    }
    //    return false;
    try {
      var publicKey = jwtConfigProperties.getPublicKey();

      Jws<Claims> claimsJws = Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token);

      Claims claims = claimsJws.getPayload();

      return true;
    } catch (JwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      return false;
    }
  }

  //  public Authentication getAuthentication(String token) {
  //    Claims claims =
  // Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
  //
  //    Object authoritiesClaim = claims.get(AUTHORITIES_KEY);
  //
  //    Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ?
  // AuthorityUtils.NO_AUTHORITIES
  //            : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());
  //
  //    User principal = new User(claims.getSubject(), "", authorities);
  //
  //    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  //  }

}
