package com.grupo3.postech.jilocomjurubeba.application.mapper.tipoUsuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;

public class TipoUsuarioMapper {

  private TipoUsuarioMapper() {}

  public static TipoUsuarioOutput paraOutput(TipoUsuario tipoUsuario) {
    return new TipoUsuarioOutput(
        tipoUsuario.getId(),
        tipoUsuario.getNome(),
        tipoUsuario.getDescricao(),
        tipoUsuario.isAtivo());
  }
}
