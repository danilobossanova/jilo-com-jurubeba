package com.grupo3.postech.jilocomjurubeba.application.mapper.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioResumoOutput;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;

public class UsuarioMapper {

  public static UsuarioOutput toOutput(Usuario usuario) {
    return new UsuarioOutput(
        usuario.getId(),
        usuario.getNome(),
        usuario.getCpf().getNumero(),
        usuario.getEmail().getEmail(),
        usuario.getTelefone(),
        usuario.getTipoUsuario().getId(),
        usuario.getTipoUsuario().getNome(),
        usuario.isAtivo());
  }

  public static UsuarioResumoOutput toResumo(Usuario usuario) {
    return new UsuarioResumoOutput(
        usuario.getId(), usuario.getNome(), usuario.getEmail().getEmail());
  }
}
