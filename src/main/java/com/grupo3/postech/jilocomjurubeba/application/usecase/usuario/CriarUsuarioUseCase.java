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

        // 1) validação de payload
        Map<String, String> erros = new HashMap<>();

        String cpfRaw = input.cpf();
        if (cpfRaw == null || cpfRaw.isBlank()) erros.put("cpf", "cpf é obrigatório");

        String emailRaw = input.email();
        if (emailRaw == null || emailRaw.isBlank()) erros.put("email", "email é obrigatório");

        String nomeRaw = input.nome();
        if (nomeRaw == null || nomeRaw.isBlank()) erros.put("nome", "nome é obrigatório");

        String senhaRaw = input.senha();
        if (senhaRaw == null || senhaRaw.isBlank()) erros.put("senha", "senha é obrigatória");

        if (!erros.isEmpty()) {
            throw new ValidacaoException("Dados inválidos", erros);
        }

        // 2) normalizações
        String cpfNormalizado = cpfRaw.replaceAll("\\D", "");
        String emailNormalizado = emailRaw.trim().toLowerCase();

        if (cpfNormalizado.length() != 11) {
            throw new ValidacaoException("Dados inválidos", Map.of("cpf", "cpf deve conter 11 dígitos"));
        }

        // 3) regras de negócio
        if (usuarioGateway.findByCpf(cpfNormalizado).isPresent()) {
            throw new RegraDeNegocioException("Usuário já cadastrado");
        }

        TipoUsuario tipoUsuario = tipoUsuarioGateway
            .buscarPorId(input.tipoUsuarioId())
            .orElseThrow(() -> new EntidadeNaoEncontradaException("TipoUsuario", input.tipoUsuarioId()));

        Usuario usuario = new Usuario(
            nomeRaw.trim(),
            new Cpf(cpfNormalizado),
            new Email(emailNormalizado),
            input.telefone(),
            tipoUsuario,
            new ArrayList<>(),
            senhaRaw
        );

        Usuario salvo = usuarioGateway.saveUsuario(usuario);
        return toOutput(salvo);
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
