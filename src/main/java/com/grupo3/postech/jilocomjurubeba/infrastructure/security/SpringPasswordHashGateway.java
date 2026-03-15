package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.PasswordHashGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringPasswordHashGateway implements PasswordHashGateway {

  private final PasswordEncoder passwordEncoder;

  public SpringPasswordHashGateway(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String hash(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }
}
