package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import java.util.List;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCaseSemEntrada;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
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
            .map(this::toOutput)
            .toList();
    }

    private UsuarioOutput toOutput(Usuario usuario) {
        Long tipoId = usuario.getTipoUsuario() == null ? null : usuario.getTipoUsuario().getId();
        String tipoNome = usuario.getTipoUsuario() == null ? null : usuario.getTipoUsuario().getNome();

        return new UsuarioOutput(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf().getNumero(),
            usuario.getEmail().getEmail(),
            usuario.getTelefone(),
            tipoId,
            tipoNome,
            usuario.isAtivo()
        );
    }
}
