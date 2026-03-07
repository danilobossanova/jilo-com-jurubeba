package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

public class BuscarTipoUsuarioPorIdUseCase implements UseCase<Long, TipoUsuarioOutput> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public BuscarTipoUsuarioPorIdUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public TipoUsuarioOutput executar(Long id) {
        return tipoUsuarioGateway
                .buscarPorId(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", id))
                .paraOutput();
    }
}