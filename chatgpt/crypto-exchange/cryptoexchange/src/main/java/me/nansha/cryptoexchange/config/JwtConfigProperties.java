package me.nansha.cryptoexchange.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "jwt")
@EnableConfigurationProperties(JwtConfigProperties.class)
@Component
public class JwtConfigProperties {
  private RSAPublicKey publicKey;
  private RSAPrivateKey privateKey;

  public RSAPublicKey getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(RSAPublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public RSAPrivateKey getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(RSAPrivateKey privateKey) {
    this.privateKey = privateKey;
  }
}
