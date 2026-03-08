package com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario;

import java.util.List;
import java.util.Optional;

import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;

public interface UsuarioGateway {

    Usuario saveUsuario(Usuario usuario);

    Optional<Usuario> findByIdUsuario(Long id);

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAllUsuario();

    void deleteUsuario(Long id);

    void deletarUsuario(Long id);
}
