package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

public class BuscarUsuarioUseCase implements UseCase<Long, UsuarioOutput> {

    private final UsuarioGatewayDomain usuarioGateway;

    public BuscarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public UsuarioOutput executar(Long id) {
        return usuarioGateway.findByIdUsuario(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id))
                .paraOutput();
    }
}