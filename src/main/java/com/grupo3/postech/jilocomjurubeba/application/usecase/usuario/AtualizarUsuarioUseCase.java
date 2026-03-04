package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGatewayDomain;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

public class AtualizarUsuarioUseCase implements UseCase<AtualizarUsuarioUseCase.Input, UsuarioOutput> {

    public record Input(Long id, AtualizarUsuarioInput input) {}

    private final UsuarioGatewayDomain usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public AtualizarUsuarioUseCase(UsuarioGatewayDomain usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
        this.usuarioGateway = usuarioGateway;
        this.tipoUsuarioGateway = tipoUsuarioGateway;
    }

    public UsuarioOutput executar(Long id, AtualizarUsuarioInput input) {
        return executar(new Input(id, input));
    }

    @Override
    public UsuarioOutput executar(Input wrapper) {
        Long id = wrapper.id();
        AtualizarUsuarioInput input = wrapper.input();

        if (id == null) throw new RegraDeNegocioException("id é obrigatório");
        if (input == null) throw new RegraDeNegocioException("dados para atualização são obrigatórios");

        Usuario atual = usuarioGateway.findByIdUsuario(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuario", id));

        // Decide novos valores (se vier nulo, mantém o atual)
        String nomeNovo = hasText(input.nome()) ? input.nome().trim() : atual.getNome();

        String telefoneNovo = input.telefone() != null && hasText(input.telefone())
                ? input.telefone().trim()
                : atual.getTelefone();

        Cpf cpfNovo = atual.getCpf();
        if (input.cpf() != null && hasText(input.cpf())) {
            String cpfStr = onlyDigits(input.cpf());
            String cpfAtual = atual.getCpf() != null ? atual.getCpf().getNumero() : null;

            if (cpfAtual == null || !cpfStr.equals(cpfAtual)) {
                usuarioGateway.findByCpf(cpfStr).ifPresent(outro -> {
                    if (outro.getId() != null && !outro.getId().equals(atual.getId())) {
                        throw new RegraDeNegocioException("cpf já cadastrado");
                    }
                });
                cpfNovo = new Cpf(cpfStr);
            }
        }

        Email emailNovo = atual.getEmail();
        if (input.email() != null && hasText(input.email())) {
            String emailStr = input.email().trim().toLowerCase();
            String emailAtual = atual.getEmail() != null ? atual.getEmail().getEmail() : null;

            if (emailAtual == null || !emailStr.equalsIgnoreCase(emailAtual)) {
                usuarioGateway.findByEmail(emailStr).ifPresent(outro -> {
                    if (outro.getId() != null && !outro.getId().equals(atual.getId())) {
                        throw new RegraDeNegocioException("email já cadastrado");
                    }
                });
                emailNovo = new Email(emailStr);
            }
        }

        TipoUsuario tipoNovo = atual.getTipoUsuario();
        if (input.tipoUsuarioId() != null) {
            tipoNovo = tipoUsuarioGateway.buscarPorId(input.tipoUsuarioId())
                    .orElseThrow(() -> new RegraDeNegocioException("Tipo de usuário não encontrado"));
        }

        // Atualiza NO DOMÍNIO (sem setters) :contentReference[oaicite:3]{index=3}
        atual.atualizarDados(nomeNovo, cpfNovo, emailNovo, telefoneNovo, tipoNovo);

        Usuario salvo = usuarioGateway.saveUsuario(atual);

        Long tipoId = salvo.getTipoUsuario() == null ? null : salvo.getTipoUsuario().getId();
        String tipoNome = salvo.getTipoUsuario() == null ? null : salvo.getTipoUsuario().getNome();

        return new UsuarioOutput(
                salvo.getId(),
                salvo.getNome(),
                salvo.getCpf().getNumero(),
                salvo.getEmail().getEmail(),
                salvo.getTelefone(),
                tipoId,
                tipoNome,
                salvo.isAtivo()
        );
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String onlyDigits(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }
}