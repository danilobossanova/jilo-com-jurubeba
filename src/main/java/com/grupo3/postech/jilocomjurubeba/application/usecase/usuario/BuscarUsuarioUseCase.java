package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

public class BuscarUsuarioUseCase implements UseCase<Long, UsuarioOutput> {

    private final UsuarioGatewayDomain usuarioGateway;

    public BuscarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public UsuarioOutput executar(Long id) {
        Usuario usuario =
            usuarioGateway.findByIdUsuario(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));

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
