package com.grupo3.postech.jilocomjurubeba.application.usecase.tipousuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.tipousuario.TipoUsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;

public class ListarTiposUsuarioUseCase implements UseCaseSemEntrada<List<TipoUsuarioOutput>> {

    private final TipoUsuarioGateway tipoUsuarioGateway;

    public ListarTiposUsuarioUseCase(TipoUsuarioGateway tipoUsuarioGateway) {
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public List<TipoUsuarioOutput> executar() {
        return tipoUsuarioGateway
                .listarTodos()
                .stream()
                .map(tipoUsuario -> tipoUsuario.paraOutput())
                .toList();
    }
}