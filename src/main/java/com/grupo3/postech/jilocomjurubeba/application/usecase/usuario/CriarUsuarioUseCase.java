package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.CriarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.ValidacaoException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

public class CriarUsuarioUseCase implements UseCase<CriarUsuarioInput, UsuarioOutput> {

    private final UsuarioGatewayDomain usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public CriarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    @Override
    public UsuarioOutput executar(CriarUsuarioInput input) {
        Map<String, String> erros = new HashMap<>();

        if (input.cpf() == null || input.cpf().isBlank()) erros.put("cpf", "cpf é obrigatório");
        if (input.email() == null || input.email().isBlank()) erros.put("email", "email é obrigatório");
        if (input.nome() == null || input.nome().isBlank()) erros.put("nome", "nome é obrigatório");
        if (input.senha() == null || input.senha().isBlank()) erros.put("senha", "senha é obrigatória");

        if (!erros.isEmpty()) {
            throw new ValidacaoException("Dados inválidos", erros);
        }

        String cpfNormalizado = input.cpf().replaceAll("\\D", "");
        String emailNormalizado = input.email().trim().toLowerCase();

        if (cpfNormalizado.length() != 11) {
            throw new ValidacaoException("Dados inválidos", Map.of("cpf", "cpf deve conter 11 dígitos"));
        }

        if (usuarioGateway.findByCpf(cpfNormalizado).isPresent()) {
            throw new RegraDeNegocioException("Usuário já cadastrado");
        }

        if (usuarioGateway.findByEmail(emailNormalizado).isPresent()) {
            throw new RegraDeNegocioException("Email já cadastrado");
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway
                .buscarPorId(input.tipoUsuarioId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", input.tipoUsuarioId()));

        Usuario usuario = new Usuario(
                input.nome(),
                new Cpf(cpfNormalizado),
                new Email(emailNormalizado),
                input.telefone(),
                tipoUsuario,
                new ArrayList<>(),
                input.senha()
        );

        Usuario salvo = usuarioGateway.saveUsuario(usuario);
        return salvo.paraOutput();
    }
}