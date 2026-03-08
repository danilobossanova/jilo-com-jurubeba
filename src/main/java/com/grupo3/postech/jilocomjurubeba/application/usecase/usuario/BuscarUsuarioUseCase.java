package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.usuario.UsuarioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

public class BuscarUsuarioUseCase implements UseCase<Long, UsuarioOutput> {

  private final UsuarioGateway usuarioGateway;

  public BuscarUsuarioUseCase(UsuarioGateway usuarioGateway) {
    this.usuarioGateway = usuarioGateway;
  }

  @Override
  public UsuarioOutput executar(Long id) {
    return UsuarioMapper.toOutput(
        usuarioGateway
            .findByIdUsuario(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id)));
  }
}
