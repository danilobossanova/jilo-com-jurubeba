package com.grupo3.postech.jilocomjurubeba.infrastructure.security;

import com.grupo3.postech.jilocomjurubeba.infrastructure.persistence.usuario.repository.UsuarioJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!test")
public class PasswordHashBackfillRunner implements ApplicationRunner {

  private static final Logger log = LoggerFactory.getLogger(PasswordHashBackfillRunner.class);

  private final UsuarioJpaRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;

  public PasswordHashBackfillRunner(
      UsuarioJpaRepository usuarioRepository, PasswordEncoder passwordEncoder) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    int atualizados = 0;

    for (var usuario : usuarioRepository.findAll()) {
      String senha = usuario.getSenhaHash();
      if (senha == null || senha.isBlank() || isBcrypt(senha)) {
        continue;
      }

      usuario.setSenhaHash(passwordEncoder.encode(senha));
      atualizados++;
    }

    if (atualizados > 0) {
      log.warn("Backfill de senha executado: {} usuario(s) com senha em texto puro convertidos.", atualizados);
    }
  }

  private boolean isBcrypt(String value) {
    return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
  }
}
