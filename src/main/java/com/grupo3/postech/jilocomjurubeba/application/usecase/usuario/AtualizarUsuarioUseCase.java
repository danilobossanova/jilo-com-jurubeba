package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;

import java.util.ArrayList;

public class AtualizarUsuarioUseCase implements UseCase<AtualizarUsuarioInput, UsuarioOutput> {

    private final UsuarioGatewayDomain usuarioGateway;

    public AtualizarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public UsuarioOutput executar(AtualizarUsuarioInput input) {
        Usuario usuario = usuarioGateway.findByIdUsuario(input.id())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", input.id()));

        usuario.atualizarDados(
            input.nome(),
            input.cpf(),
            input.email(),
            input.telefone(),
            input.typeUsuario()
        );

        Usuario atualizado = usuarioGateway.saveUsuario(usuario);
        return toOutput(atualizado);
    }

    private UsuarioOutput toOutput(Usuario usuario) {
        return new UsuarioOutput(
            usuario.getId(),
            usuario.getNome(),
            usuario.getCpf(),
            usuario.getEmail(),
            usuario.getTelefone(),
            usuario.getTypeUsuario(),
            usuario.getRestaurantes(),
            usuario.isAtivo()
        );
    }


}
