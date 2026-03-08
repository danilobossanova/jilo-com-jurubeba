package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

public class DeletarTipoUsuarioUseCase implements UseCaseSemSaida<Long> {

  private final TipoUsuarioGateway tipoUsuarioGateway;

  public DeletarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
    this.tipoUsuarioGateway = tipoUsuarioGateway;
  }

  @Override
  public void executar(Long id) {
    TipoUsuario tipoUsuario =
        tipoUsuarioGateway
            .buscarPorId(id)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", id));

    tipoUsuario.desativar();
    tipoUsuarioGateway.salvar(tipoUsuario);
  }
}
