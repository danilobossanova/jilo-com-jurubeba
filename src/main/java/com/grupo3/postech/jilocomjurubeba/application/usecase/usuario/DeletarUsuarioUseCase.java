package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemSaida;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

public class DeletarUsuarioUseCase implements UseCaseSemSaida<Long> {

    private final UsuarioGatewayDomain usuarioGateway;

    public DeletarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public void executar(Long id) {
        Usuario usuario =
            usuarioGateway.findByIdUsuario(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));

        usuarioGateway.deletarUsuario(usuario.getId());
    }
}
