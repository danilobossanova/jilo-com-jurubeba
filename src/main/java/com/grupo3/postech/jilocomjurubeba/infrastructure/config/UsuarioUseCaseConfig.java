package com.grupo3.postech.jilocomjurubeba.infrastructure.config;

import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.BuscarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.CriarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.DeletarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.application.usecase.usuario.ListarUsuarioUseCase;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioUseCaseConfig {

  @Bean
  public CriarUsuarioUseCase criarUsuarioUseCase(
      UsuarioGateway usuarioGateway,
      TipoUsuarioGateway tipoUsuarioGateway,
      PasswordEncoder passwordEncoder) {
    return new CriarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway, passwordEncoder);
  }

  @Bean
  public AtualizarUsuarioUseCase atualizarUsuarioUseCase(
      UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
    return new AtualizarUsuarioUseCase(usuarioGateway, tipoUsuarioGateway);
  }

  @Bean
  public BuscarUsuarioUseCase buscarUsuarioUseCase(UsuarioGateway usuarioGateway) {
    return new BuscarUsuarioUseCase(usuarioGateway);
  }

  @Bean
  public ListarUsuarioUseCase listarUsuarioUseCase(UsuarioGateway usuarioGateway) {
    return new ListarUsuarioUseCase(usuarioGateway);
  }

  @Bean
  public DeletarUsuarioUseCase deletarUsuarioUseCase(UsuarioGateway usuarioGateway) {
    return new DeletarUsuarioUseCase(usuarioGateway);
  }
}
