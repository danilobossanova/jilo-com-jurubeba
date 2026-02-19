package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.restaurante.Restaurante;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

import java.util.ArrayList;
import java.util.List;

public class CriarUsuarioUseCase implements UseCase<CriarUsuarioInput, UsuarioOutput> {

    private final UsuarioGatewayDomain usuarioGateway;

    public CriarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public UsuarioOutput executar(CriarUsuarioInput input) {

        if (usuarioGateway.findByCpf(input.cpf().getCpf().trim().toUpperCase()).isPresent()) {
            throw new RegraDeNegocioException("Usuario ja cadastrado");
        }

        Usuario usuario = new Usuario(
            input.nome(),
            new Cpf(input.cpf().getCpf().trim().toUpperCase()),
            new Email(input.email().getEmail().trim().toUpperCase()),
            input.telefone(),
            input.typeUsuario(),
            new ArrayList<>());

        Usuario salvo = usuarioGateway.saveUsuario(usuario);

        return toOutput(salvo);


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
