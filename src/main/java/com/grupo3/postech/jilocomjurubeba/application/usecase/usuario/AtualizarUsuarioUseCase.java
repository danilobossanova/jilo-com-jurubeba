package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.mapper.usuario.UsuarioMapper;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.tipousuario.TipoUsuario;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.exception.RegraDeNegocioException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.tipousuario.TipoUsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Cpf;
import com.grupo3.postech.jilocomjurubeba.domain.valueobject.Email;

public class AtualizarUsuarioUseCase implements UseCase<AtualizarUsuarioUseCase.Input, UsuarioOutput> {

    public record Input(Long id, AtualizarUsuarioInput input) {}

    private final UsuarioGateway usuarioGateway;
    private final TipoUsuarioGateway tipoUsuarioGateway;

    public AtualizarUsuarioUseCase(UsuarioGateway usuarioGateway, TipoUsuarioGateway tipoUsuarioGateway) {
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

        Usuario.UsuarioSnapshot dadosAtuais = atual.snapshot();

        String nomeNovo = hasText(input.nome()) ? input.nome().trim() : dadosAtuais.nome();
        String telefoneNovo = hasText(input.telefone()) ? input.telefone().trim() : dadosAtuais.telefone();

        Cpf cpfNovo = new Cpf(dadosAtuais.cpf());
        if (hasText(input.cpf())) {
            String cpfStr = onlyDigits(input.cpf());

            if (!atual.usaCpf(cpfStr)) {
                usuarioGateway.findByCpf(cpfStr).ifPresent(outro -> {
                    if (!outro.representaMesmoUsuarioQue(atual)) {
                        throw new RegraDeNegocioException("cpf já cadastrado");
                    }
                });
                cpfNovo = new Cpf(cpfStr);
            }
        }

        Email emailNovo = new Email(dadosAtuais.email());
        if (hasText(input.email())) {
            String emailStr = input.email().trim().toLowerCase();

            if (!atual.usaEmail(emailStr)) {
                usuarioGateway.findByEmail(emailStr).ifPresent(outro -> {
                    if (!outro.representaMesmoUsuarioQue(atual)) {
                        throw new RegraDeNegocioException("email já cadastrado");
                    }
                });
                emailNovo = new Email(emailStr);
            }
        }

        TipoUsuario tipoNovo = atual.eDonoDeRestaurante() || atual.eCliente()
                ? tipoUsuarioGateway.buscarPorId(dadosAtuais.tipoUsuarioId())
                    .orElseThrow(() -> new RegraDeNegocioException("Tipo de usuário atual não encontrado"))
                : tipoUsuarioGateway.buscarPorId(dadosAtuais.tipoUsuarioId())
                    .orElseThrow(() -> new RegraDeNegocioException("Tipo de usuário atual não encontrado"));

        if (input.tipoUsuarioId() != null && !input.tipoUsuarioId().equals(dadosAtuais.tipoUsuarioId())) {
            tipoNovo = tipoUsuarioGateway.buscarPorId(input.tipoUsuarioId())
                    .orElseThrow(() -> new RegraDeNegocioException("Tipo de usuário não encontrado"));
        }

        atual.atualizarCadastro(nomeNovo, cpfNovo, emailNovo, telefoneNovo, tipoNovo);

        Usuario salvo = usuarioGateway.saveUsuario(atual);
        return UsuarioMapper.toOutput(salvo);
    }

    private boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String onlyDigits(String s) {
        return s == null ? "" : s.replaceAll("\\D", "");
    }
}
