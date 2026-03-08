package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.usuario.UsuarioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

public class ListarUsuarioUseCase implements UseCaseSemEntrada<List<UsuarioOutput>> {

    private final UsuarioGateway usuarioGateway;

    public ListarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public List<UsuarioOutput> executar() {
        return usuarioGateway.findAllUsuario()
                .stream()
            .map(UsuarioMapper::toOutput)
                .toList();
    }
}
