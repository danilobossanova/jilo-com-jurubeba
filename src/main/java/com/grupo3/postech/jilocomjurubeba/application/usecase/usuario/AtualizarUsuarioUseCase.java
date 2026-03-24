package com.grupo3.postech.jilocomjurubeba.application.usecase.usuario;

import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.AtualizarUsuarioInput;
import com.grupo3.postech.jilocomjurubeba.application.dto.usuario.UsuarioOutput;
import com.grupo3.postech.jilocomjurubeba.application.usecase.UseCase;
import com.grupo3.postech.jilocomjurubeba.domain.entity.usuario.Usuario;
import com.grupo3.postech.jilocomjurubeba.domain.exception.EntidadeNaoEncontradaException;
import com.grupo3.postech.jilocomjurubeba.domain.gateway.usuario.UsuarioGateway;

/**
 * Caso de uso para atualizacao de dados de um usuario existente.
 *
 * <p>Este caso de uso orquestra o fluxo de atualizacao de usuario:
 *
 * <ol>
 *   <li>Busca o usuario pelo ID informado no input
 *   <li>Aplica atualizacao parcial: campos nulos no input preservam os valores atuais
 *   <li>Delega a atualizacao para o metodo {@code atualizarDados()} da entidade de dominio
 *   <li>Persiste a entidade atualizada via {@link UsuarioGateway#salvar(Usuario)}
 *   <li>Retorna o {@link UsuarioOutput} com os dados atualizados
 * </ol>
 *
 * <p>Na Clean Architecture, este caso de uso pertence a camada Application e depende apenas de
 * interfaces do Domain (gateways). Nao possui dependencia de framework.
 *
 * @author Grupo 3 - Tech Challenge POSTECH FIAP - Fase 2 - Data Guardian
 *     <ul>
 *       <li>Thiago de Jesus Cordeiro - Desenvolvimento e Arquitetura
 *       <li>Juliana Maria Dal Olio Braz - Desenvolvimento e Arquitetura
 *       <li>Luis Henrique Silveira Borges - Desenvolvimento e Arquitetura
 *       <li>Gilmar da Costa Moraes Junior - Desenvolvimento e Arquitetura
 *       <li>Danilo Fernando - Desenvolvimento e Arquitetura
 *     </ul>
 */
public class AtualizarUsuarioUseCase implements UseCase<AtualizarUsuarioInput, UsuarioOutput> {

    private final UsuarioGateway usuarioGateway;

    /**
     * Construtor com injecao do gateway de usuarios.
     *
     * @param usuarioGateway gateway de persistencia de usuarios
     */
    public AtualizarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * Executa a atualizacao parcial de um usuario existente.
     *
     * <p>Campos com valor {@code null} no input sao preservados com seus valores atuais. Apenas
     * campos preenchidos no input sao efetivamente alterados.
     *
     * @param input dados de entrada contendo o ID do usuario e os campos a serem atualizados
     * @return {@link UsuarioOutput} com os dados do usuario apos a atualizacao
     * @throws EntidadeNaoEncontradaException se nenhum usuario for encontrado com o ID informado
     */
    @Override
    public UsuarioOutput executar(AtualizarUsuarioInput input) {

        Usuario usuario =
                usuarioGateway
                        .buscarPorId(input.id())
                        .orElseThrow(
                                () -> new EntidadeNaoEncontradaException("Usuario", input.id()));

        String nomeAtualizado = input.nome() != null ? input.nome() : usuario.getNome();
        String emailAtualizado =
                input.email() != null ? input.email() : usuario.getEmail().getEndereco();
        String telefoneAtualizado =
                input.telefone() != null ? input.telefone() : usuario.getTelefone();

        usuario.atualizarDados(nomeAtualizado, emailAtualizado, telefoneAtualizado);

        Usuario salvo = usuarioGateway.salvar(usuario);

        return toOutput(salvo);
    }

    /**
     * Converte uma entidade de dominio {@link Usuario} para o DTO de saida.
     *
     * @param u entidade de dominio a ser convertida
     * @return {@link UsuarioOutput} com os dados da entidade
     */
    private static UsuarioOutput toOutput(Usuario u) {
        return new UsuarioOutput(
                u.getId(),
                u.getNome(),
                u.getCpf().getNumero(),
                u.getEmail().getEndereco(),
                u.getTelefone(),
                u.getTipoUsuario() != null ? u.getTipoUsuario().getNome() : null,
                u.isAtivo());
    }
}
