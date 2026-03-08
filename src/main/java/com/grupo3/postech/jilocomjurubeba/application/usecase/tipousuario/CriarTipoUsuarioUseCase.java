package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.CriarTipoUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.tipoUsuario.TipoUsuarioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

public class CriarTipoUsuarioUseCase implements UseCase<CriarTipoUsuarioInput, TipoUsuarioOutput> {

  private final TipoUsuarioGateway tipoUsuarioGateway;

  public CriarTipoUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
    this.tipoUsuarioGateway = tipoUsuarioGateway;
  }

  @Override
  public TipoUsuarioOutput executar(CriarTipoUsuarioInput input) {

    String nomeNormalizado = input.nome().trim().toUpperCase();

    if (tipoUsuarioGateway.existePorNome(nomeNormalizado)) {
      throw new RegraDeNegocioException(
          "Ja existe um tipo de usuario com o nome '" + input.nome() + "'");
    }

    TipoUsuario tipoUsuario = new TipoUsuario(input.nome(), input.descricao());

    TipoUsuario salvo = tipoUsuarioGateway.salvar(tipoUsuario);

    return TipoUsuarioMapper.paraOutput(salvo);
  }
}
