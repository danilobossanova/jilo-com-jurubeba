package com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioGatewayDomain {

    Usuario saveUsuario(Usuario usuario);

    Optional<Usuario> findByIdUsuario(Long id);

    Optional<Usuario> findByCpf(String cpf);

    List<Usuario> findAllUsuario();

    void deleteUsuario();



}
