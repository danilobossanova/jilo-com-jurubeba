package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

public class ListarUsuarioUseCase implements UseCaseSemEntrada<List<UsuarioOutput>> {

    private final UsuarioGatewayDomain usuarioGateway;

    public ListarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public List<UsuarioOutput> executar() {
        return usuarioGateway.findAllUsuario()
                .stream()
                .map(usuario -> usuario.paraOutput())
                .toList();
    }
}